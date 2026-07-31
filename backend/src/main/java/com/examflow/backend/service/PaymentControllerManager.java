package com.examflow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.examflow.backend.dto.BankAccountResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.PaymentsListResponse;
import com.examflow.backend.dto.BankAccountRequest;
import com.examflow.backend.dto.GeneralResponse;

@Service
public interface PaymentControllerManager {

    List<BankAccountResponse> getBankAccounts();

    GeneralResponse recordClassPayNow(Integer bankAccountId, Integer classId, MultipartFile receiptFile);

    List<PaymentsListResponse> getAllPayments();

    GeneralResponse approvePayment(Integer paymentSeq, String refferenceNumber);

    GeneralResponse rejectPayment(Integer paymentSeq, String reason);

    GeneralResponse createBankAccount(BankAccountRequest request);

    GeneralResponse updateBankAccount(Integer id, BankAccountRequest request);

    GeneralResponse deleteBankAccount(Integer id);

}
