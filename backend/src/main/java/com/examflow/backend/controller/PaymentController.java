package com.examflow.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.examflow.backend.dto.BankAccountResponse;
import com.examflow.backend.dto.GeneralResponse;
import com.examflow.backend.dto.PaymentRequest;
import com.examflow.backend.dto.PaymentsListResponse;
import com.examflow.backend.repository.BankAccountRepository;
import com.examflow.backend.service.PaymentControllerManager;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    private final BankAccountRepository bankAccountRepository;
    private final PaymentControllerManager paymentControllerManager;

    @Autowired
    public PaymentController(BankAccountRepository bankAccountRepository,
            PaymentControllerManager paymentControllerManager) {
        this.bankAccountRepository = bankAccountRepository;
        this.paymentControllerManager = paymentControllerManager;
    }

    @GetMapping("/get-all-bankAcoounts")
    public List<BankAccountResponse> getStudents() {
        return paymentControllerManager.getBankAccounts();
    }

    @PostMapping(value = "/makeBakTransfer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GeneralResponse makeBankTransfer(
            @RequestParam("bank_account_id") Integer bankAccountId,
            @RequestParam("class_id") Integer classId,
            @RequestParam("receipt_file") MultipartFile receiptFile) {

        GeneralResponse generalResponse = new GeneralResponse();
        generalResponse = paymentControllerManager.recordClassPayNow(bankAccountId, classId, receiptFile);
        return generalResponse;
    }

    @GetMapping("/get-all-payments")
    public List<PaymentsListResponse> getAllPayments() {
        return paymentControllerManager.getAllPayments();
    }

    @PostMapping("/{paymentId}/approve")
    public GeneralResponse approvePayment(@PathVariable Integer paymentId,
            @RequestBody PaymentRequest paymentRequest) {
        System.out.println(paymentId);
        System.out.println(paymentRequest.getReference_number());
        return paymentControllerManager.approvePayment(paymentId, paymentRequest.getReference_number());

    }

    @PostMapping("/{paymentId}/reject")
    public GeneralResponse rejectPayment(@PathVariable Integer paymentId,
            @RequestBody PaymentRequest paymentRequest) {
        System.out.println(paymentId);
        System.out.println(paymentRequest.getRejection_reason());
        return paymentControllerManager.rejectPayment(paymentId, paymentRequest.getRejection_reason());

    }

}
