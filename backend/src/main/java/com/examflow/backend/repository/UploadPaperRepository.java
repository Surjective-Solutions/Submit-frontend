package com.examflow.backend.repository;

import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.UplaodPaper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadPaperRepository extends JpaRepository<UplaodPaper, Integer> {

    List<UplaodPaper> findByClassesAndStatus(Classes classes, Integer status);

    List<UplaodPaper> findByClassesAndStatusAndClassPaymentRecord(Classes classes, Integer status,
            ClassPaymentRecord classPaymentRecord);
}
