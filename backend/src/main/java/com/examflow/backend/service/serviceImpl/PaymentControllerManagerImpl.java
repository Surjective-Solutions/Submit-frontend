package com.examflow.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

import com.examflow.backend.dto.BankAccountResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.PaymentsListResponse;
import com.examflow.backend.dto.BankAccountRequest;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.entity.BankAccount;
import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClassPaymentRecord;
import com.examflow.backend.enums.paymentStatus;
import com.examflow.backend.repository.BankAccountRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.StudentClassPaymentRecordsRepository;
import com.examflow.backend.repository.StudentRepository;
import com.examflow.backend.service.FileStorageService;
import com.examflow.backend.service.PaymentControllerManager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Null;

@Service
public class PaymentControllerManagerImpl implements PaymentControllerManager {

    private final BankAccountRepository bankAccountRepository;

    private final StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository;

    private final ClassesRepository classesRepository;

    private final FileStorageService fileStorageService;

    private final StudentRepository studentRepository;

    private HttpServletRequest request;

    private final MonthlyPaymentService monthlyPaymentService;

    public PaymentControllerManagerImpl(BankAccountRepository bankAccountRepository,
            HttpServletRequest request,
            FileStorageService fileStorageService,
            StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository,
            StudentRepository studentRepository,
            ClassesRepository classesRepository,
            MonthlyPaymentService monthlyPaymentService) {
        this.bankAccountRepository = bankAccountRepository;
        this.studentClassPaymentRecordsRepository = studentClassPaymentRecordsRepository;
        this.studentRepository = studentRepository;
        this.fileStorageService = fileStorageService;
        this.classesRepository = classesRepository;
        this.request = request;
        this.monthlyPaymentService = monthlyPaymentService;
    }

    @Override
    public List<BankAccountResponse> getBankAccounts() {

        List<BankAccount> existsBankAccounts = bankAccountRepository.findByStatus(2);
        List<BankAccountResponse> bankAccountResponses = new ArrayList<>();
        for (BankAccount bankAccount : existsBankAccounts) {
            BankAccountResponse bankAccountResponse = new BankAccountResponse();
            bankAccountResponse.setId(bankAccount.getBankAccountSeq());
            bankAccountResponse.setAccountName(bankAccount.getAccountName());
            bankAccountResponse.setAccountNumber(bankAccount.getAccountNumber());
            bankAccountResponse.setAdditionalDetails(bankAccount.getAdditionalDetails());
            bankAccountResponse.setBankName(bankAccount.getDisplayName());

            bankAccountResponses.add(bankAccountResponse);

        }

        return bankAccountResponses;
    }

    @Override
    public GeneralResponse recordClassPayNow(Integer bankAccountId, Integer classId, MultipartFile receiptFile) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Integer studentSeq = (Integer) request.getAttribute("userId");
        Student student = studentRepository.findByStudentSeq(studentSeq);

        GeneralResponse response = new GeneralResponse();
        Integer currentYear = LocalDateTime.now().getYear();
        Integer currentMonth = LocalDateTime.now().getMonthValue();
        Classes classes = classesRepository.findByClassSeqAndStatus(classId, 2);
        if (classes == null) {
            response.setIsSuccess(false);
            response.setMessage("Class not found");
            return response;
        }

        ClassPaymentRecord classPaymentRecord = monthlyPaymentService.getOrCreateClassPaymentRecord(classes,
                currentMonth, currentYear, username);

        StudentClassPaymentRecord studentClassPaymentRecord = studentClassPaymentRecordsRepository
                .findByStudentAndStatusAndClassPaymentRecord(student, 2, classPaymentRecord);
        if (studentClassPaymentRecord != null) {

            studentClassPaymentRecord.setPayedAmount(classPaymentRecord.getClasses().getMonthlyFee());
            studentClassPaymentRecord.setPayedBy(username);
            studentClassPaymentRecord.setPayedTime(LocalDateTime.now());
            studentClassPaymentRecord.setIsApproved(null);
            studentClassPaymentRecord.setIsForPayments(true);

            studentClassPaymentRecordsRepository.save(studentClassPaymentRecord);

            String fileName = fileStorageService.savePaymentReceipt(receiptFile);
            studentClassPaymentRecord.setReciptPath("/uploads/payment_recipts/" + fileName);

            studentClassPaymentRecordsRepository.save(studentClassPaymentRecord);

            response.setIsSuccess(true);
            response.setMessage("student class payment recorded  successsfully for "
                    + studentClassPaymentRecord.getClassPaymentRecord().getMonth()
                    + +studentClassPaymentRecord.getClassPaymentRecord().getYear() + "file Uplaoded Succesfully");

            return response;
        }

        response.setIsSuccess(false);
        response.setMessage("student class payment record not found");

        return response;
    }

    @Override
    public List<PaymentsListResponse> getAllPayments() {

        List<PaymentsListResponse> paymentsListResponses = new ArrayList<>();
        List<StudentClassPaymentRecord> studentClassPaymentRecords = studentClassPaymentRecordsRepository
                .findByStatusNotAndIsForPayments(0, true);

        for (StudentClassPaymentRecord paymentRecord : studentClassPaymentRecords) {
            PaymentsListResponse oneRecord = new PaymentsListResponse();
            oneRecord.setId(paymentRecord.getStudentClassPaymentRecordSeq());
            oneRecord.setStudent_id(paymentRecord.getStudent().getStudentSeq());
            oneRecord.setStudent_name(
                    paymentRecord.getStudent().getFirstName() + paymentRecord.getStudent().getLastName());
            oneRecord.setStudent_number(null);// need to implement
            oneRecord.setTeacher_name(paymentRecord.getClassPaymentRecord().getClasses().getTutor().getName());
            oneRecord.setClass_name(paymentRecord.getClassPaymentRecord().getClasses().getDisplayName());
            oneRecord.setAmount(paymentRecord.getPayedAmount());
            oneRecord.setRejection_reason(paymentRecord.getReson());
            oneRecord.setReceipt_url(paymentRecord.getReciptPath());
            if (paymentRecord.getIsApproved() == null) {
                oneRecord.setStatus("PENDING");
            } else if (paymentRecord.getIsApproved() == true) {
                oneRecord.setStatus("APPROVED");
            } else {
                oneRecord.setStatus("REJECTED");
            }

            oneRecord.setReference_number(paymentRecord.getReffrenceNo());
            oneRecord.setSubmitted_at(paymentRecord.getPayedTime());
            oneRecord.setReviewed_at(paymentRecord.getApprovedTime());
            oneRecord.setReviewed_by(paymentRecord.getApprovedBy());

            paymentsListResponses.add(oneRecord);
        }

        return paymentsListResponses;
    }

    @Override
    public GeneralResponse approvePayment(Integer paymentSeq, String refferenceNumber) {

        GeneralResponse generalResponse = new GeneralResponse();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        StudentClassPaymentRecord studentClassPaymentRecord = studentClassPaymentRecordsRepository
                .findByStudentClassPaymentRecordSeq(paymentSeq);
        if (studentClassPaymentRecord != null) {
            studentClassPaymentRecord.setIsPayed(true);
            studentClassPaymentRecord.setIsApproved(true);
            studentClassPaymentRecord.setApprovedBy(username);
            studentClassPaymentRecord.setApprovedTime(LocalDateTime.now());
            studentClassPaymentRecord.setReffrenceNo(refferenceNumber);

            studentClassPaymentRecordsRepository.save(studentClassPaymentRecord);
            generalResponse.setIsSuccess(true);
            generalResponse.setMessage("Payment Approved Successfully.");
            return generalResponse;
        }

        generalResponse.setIsSuccess(false);
        generalResponse.setMessage("Payment record does not found.");

        return generalResponse;
    }

    @Override
    public GeneralResponse rejectPayment(Integer paymentSeq, String reason) {
        GeneralResponse generalResponse = new GeneralResponse();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        StudentClassPaymentRecord studentClassPaymentRecord = studentClassPaymentRecordsRepository
                .findByStudentClassPaymentRecordSeq(paymentSeq);
        if (studentClassPaymentRecord != null) {
            studentClassPaymentRecord.setIsPayed(false);
            studentClassPaymentRecord.setIsApproved(false);
            studentClassPaymentRecord.setApprovedBy(username);
            studentClassPaymentRecord.setApprovedTime(LocalDateTime.now());
            studentClassPaymentRecord.setStatus(paymentStatus.fromStatusName("REJECTED").getSequence());
            studentClassPaymentRecord.setReson(reason);

            studentClassPaymentRecordsRepository.save(studentClassPaymentRecord);
            generalResponse.setIsSuccess(true);
            generalResponse.setMessage("Payment Rejected Successfully.");

            StudentClassPaymentRecord newrRecord = new StudentClassPaymentRecord();

            newrRecord.setClassPaymentRecord(studentClassPaymentRecord.getClassPaymentRecord());
            newrRecord.setStudent(studentClassPaymentRecord.getStudent());
            newrRecord.setCreatedBy("SYSYTEM");
            newrRecord.setLastModifiedBy("SYSYTEM");
            newrRecord.setIsPayed(false);
            newrRecord.setCreatedDateTime(LocalDateTime.now());
            newrRecord.setLastModifiedDateTime(LocalDateTime.now());
            newrRecord.setStatus(2);

            studentClassPaymentRecordsRepository.save(newrRecord);

            return generalResponse;
        }

        generalResponse.setIsSuccess(false);
        generalResponse.setMessage("Payment record does not found.");

        return generalResponse;
    }

    @Override
    public GeneralResponse createBankAccount(BankAccountRequest request) {
        GeneralResponse response = new GeneralResponse();
        BankAccount bankAccount = new BankAccount();
        bankAccount.setDisplayName(request.getDisplayName());
        bankAccount.setAccountName(request.getAccountName());
        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setAdditionalDetails(request.getAdditionalDetails());
        bankAccount.setStatus(2);

        bankAccountRepository.save(bankAccount);
        response.setIsSuccess(true);
        response.setMessage("Bank account created successfully");
        return response;
    }

    @Override
    public GeneralResponse updateBankAccount(Integer id, BankAccountRequest request) {
        GeneralResponse response = new GeneralResponse();
        BankAccount bankAccount = bankAccountRepository.findByBankAccountSeq(id);
        if (bankAccount == null) {
            response.setIsSuccess(false);
            response.setMessage("Bank account not found");
            return response;
        }
        bankAccount.setDisplayName(request.getDisplayName());
        bankAccount.setAccountName(request.getAccountName());
        bankAccount.setAccountNumber(request.getAccountNumber());
        bankAccount.setAdditionalDetails(request.getAdditionalDetails());

        bankAccountRepository.save(bankAccount);
        response.setIsSuccess(true);
        response.setMessage("Bank account updated successfully");
        return response;
    }

    @Override
    public GeneralResponse deleteBankAccount(Integer id) {
        GeneralResponse response = new GeneralResponse();
        BankAccount bankAccount = bankAccountRepository.findByBankAccountSeq(id);
        if (bankAccount == null) {
            response.setIsSuccess(false);
            response.setMessage("Bank account not found");
            return response;
        }
        bankAccount.setStatus(1);
        bankAccountRepository.save(bankAccount);
        response.setIsSuccess(true);
        response.setMessage("Bank account deleted successfully");
        return response;
    }

}
