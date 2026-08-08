package com.examflow.backend.repository;

import com.examflow.backend.entity.GradeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeSubmissionRepository extends JpaRepository<GradeSubmission, Integer> {
    
}
