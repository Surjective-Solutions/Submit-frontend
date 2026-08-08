package com.examflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.EmailStructure;

public interface EmailStructureRepository extends JpaRepository<EmailStructure,Integer>{

    EmailStructure findByEmailNameAndStatusSeq (String emailName,Integer statusSeq);
    
}
