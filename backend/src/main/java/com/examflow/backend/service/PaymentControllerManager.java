package com.examflow.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.examflow.backend.dto.BankAccountResponse;

@Service
public interface PaymentControllerManager {

    List<BankAccountResponse> getBankAccounts();
}
