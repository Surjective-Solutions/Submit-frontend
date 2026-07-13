package com.examflow.backend.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.examflow.backend.dto.BankAccountResponse;
import com.examflow.backend.entity.BankAccount;
import com.examflow.backend.repository.BankAccountRepository;
import com.examflow.backend.service.PaymentControllerManager;

@Service
public class PaymentControllerManagerImpl implements PaymentControllerManager {

    private final BankAccountRepository bankAccountRepository;

    public PaymentControllerManagerImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
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

}
