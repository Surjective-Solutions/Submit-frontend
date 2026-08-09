package com.examflow.backend.repository;

import com.examflow.backend.entity.GradeSubmission;
import com.examflow.backend.entity.PaperSubmission;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeSubmissionRepository extends JpaRepository<GradeSubmission, Integer> {
    

    GradeSubmission findByPaperSubmissionAndStatus(PaperSubmission paperSubmission , Integer statusSeq);
}
