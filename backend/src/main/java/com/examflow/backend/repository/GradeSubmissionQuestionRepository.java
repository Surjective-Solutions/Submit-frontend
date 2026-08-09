package com.examflow.backend.repository;

import com.examflow.backend.entity.GradeSubmission;
import com.examflow.backend.entity.GradeSubmissionQuestion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeSubmissionQuestionRepository extends JpaRepository<GradeSubmissionQuestion, Integer> {
    
    List<GradeSubmissionQuestion> findByGradeSubmissionAndStatus(GradeSubmission gradeSubmission,Integer statusSeq);
}
