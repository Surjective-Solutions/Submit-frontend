package com.examflow.backend.repository;

import com.examflow.backend.entity.PaperSubmission;
import com.examflow.backend.entity.Student;
import com.examflow.backend.entity.UplaodPaper;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaperSubmissionRepository extends JpaRepository<PaperSubmission, Integer> {

    PaperSubmission findByStudentAndUplaodpaperAndStatusSeq(Student student, UplaodPaper uplaodpaper,
            Integer statusSeq);


            List<PaperSubmission> findByUplaodpaperAndStatusSeq(UplaodPaper uplaodpaper, Integer statusSeq);

            PaperSubmission findByPaperSubmissionSeq(Integer paperSubmissionSeq);
}
