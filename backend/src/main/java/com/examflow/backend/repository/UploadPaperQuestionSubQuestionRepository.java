package com.examflow.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.UploadPaperQuestionSubQuestion;

public interface UploadPaperQuestionSubQuestionRepository
        extends JpaRepository<UploadPaperQuestionSubQuestion, Integer> {

}
