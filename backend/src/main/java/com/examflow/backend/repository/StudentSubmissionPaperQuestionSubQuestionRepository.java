package com.examflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.StudentSubmissionPaperSubQuestion;

public interface StudentSubmissionPaperQuestionSubQuestionRepository extends JpaRepository<StudentSubmissionPaperSubQuestion,Integer> {
    
}
