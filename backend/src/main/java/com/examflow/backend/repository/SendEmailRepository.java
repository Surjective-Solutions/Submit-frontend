package com.examflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.SendEmail;

public interface SendEmailRepository extends JpaRepository<SendEmail,Integer>{
    
}
