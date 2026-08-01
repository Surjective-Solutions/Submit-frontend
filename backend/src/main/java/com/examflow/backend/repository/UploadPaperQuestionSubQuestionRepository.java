package com.examflow.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.UploadPaperQuestion;
import com.examflow.backend.entity.UploadPaperQuestionSubQuestion;

public interface UploadPaperQuestionSubQuestionRepository
        extends JpaRepository<UploadPaperQuestionSubQuestion, Integer> {


                 List<UploadPaperQuestionSubQuestion> findByUploadPaperQuestionAndStatus(UploadPaperQuestion uploadPaperQuestion,Integer statuSeq);
}
