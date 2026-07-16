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
import com.examflow.backend.entity.BankAccount;
import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.StudentClassPaymentRecord;
import com.examflow.backend.repository.BankAccountRepository;
import com.examflow.backend.repository.ClassPaymentRecordRepository;
import com.examflow.backend.repository.ClassesRepository;
import com.examflow.backend.repository.StudentClassPaymentRecordsRepository;
import com.examflow.backend.repository.StudentRepository;
import com.examflow.backend.service.FileStorageService;
import com.examflow.backend.service.PaymentControllerManager;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class PaymentControllerManagerImpl implements PaymentControllerManager {

    private final BankAccountRepository bankAccountRepository;

    private final StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository;

    private final ClassesRepository classesRepository;

    private final FileStorageService fileStorageService;

    private final StudentRepository studentRepository;

    private HttpServletRequest request;

    private final ClassPaymentRecordRepository classPaymentRecordRepository;

    public PaymentControllerManagerImpl(BankAccountRepository bankAccountRepository,
            ClassPaymentRecordRepository classPaymentRecordRepository,
            HttpServletRequest request,
            FileStorageService fileStorageService,
            StudentClassPaymentRecordsRepository studentClassPaymentRecordsRepository,
            StudentRepository studentRepository,
            ClassesRepository classesRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.studentClassPaymentRecordsRepository = studentClassPaymentRecordsRepository;
        this.studentRepository = studentRepository;
        this.fileStorageService = fileStorageService;
        this.classPaymentRecordRepository = classPaymentRecordRepository;
        this.classesRepository = classesRepository;
        this.request = request;
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

        ClassPaymentRecord classPaymentRecord = classPaymentRecordRepository
                .findByClassesAndMonthAndYearAndStatus(classes, currentMonth, currentYear, 2);

        StudentClassPaymentRecord studentClassPaymentRecord = studentClassPaymentRecordsRepository
                .findByStudentAndStatusAndClassPaymentRecord(student, 2, classPaymentRecord);
        if (studentClassPaymentRecord != null) {

            studentClassPaymentRecord.setPayedAmount(classPaymentRecord.getClasses().getMonthlyFee());
            studentClassPaymentRecord.setPayedBy(username);
            studentClassPaymentRecord.setPayedTime(LocalDateTime.now());
            studentClassPaymentRecord.setStatus(3);// set status to pending approved

            studentClassPaymentRecordsRepository.save(studentClassPaymentRecord);

            String fileName = fileStorageService.savePaymentReceipt(receiptFile);
            studentClassPaymentRecord.setReciptPath(System.getProperty("user.home")
                    + "/lms/uploads/payment_recipts/" + fileName);

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

}
