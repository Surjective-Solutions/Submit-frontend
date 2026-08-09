package com.examflow.backend.repository;

import com.examflow.backend.entity.ClassPaymentRecord;
import com.examflow.backend.entity.Classes;
import com.examflow.backend.entity.Tutor;
import com.examflow.backend.entity.UplaodPaper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadPaperRepository extends JpaRepository<UplaodPaper, Integer> {

    List<UplaodPaper> findByClassesAndStatus(Classes classes, Integer status);

    List<UplaodPaper> findByClassesTutorAndStatus(Tutor tutor, Integer status);

    List<UplaodPaper> findByClassesAndStatusAndClassPaymentRecordAndIsPublished(Classes classes, Integer status,
            ClassPaymentRecord classPaymentRecord,Boolean isPublished);

    UplaodPaper findByUploadPaperSeq(Integer uploadPaperSeq);
}
