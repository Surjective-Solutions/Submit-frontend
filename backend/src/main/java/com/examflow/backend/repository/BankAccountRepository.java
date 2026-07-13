package com.examflow.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.examflow.backend.entity.BankAccount;

@Service
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {

    List<BankAccount> findByStatus(Integer status);
}
