package com.examflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.StudentSubmissionPaperQuestion;

public interface StudentSubmissionPaperQuestionRepository extends JpaRepository<StudentSubmissionPaperQuestion,Integer> {
    
}
