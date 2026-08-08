package com.examflow.backend.repository;

import com.examflow.backend.entity.GradeSubmissionQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeSubmissionQuestionRepository extends JpaRepository<GradeSubmissionQuestion, Integer> {
    
}
