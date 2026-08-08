package com.examflow.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examflow.backend.entity.UplaodPaper;
import com.examflow.backend.entity.UploadPaperQuestion;

public interface UploadPaperQuestionRepository extends JpaRepository<UploadPaperQuestion, Integer> {

    List<UploadPaperQuestion> findByUplaodPaperAndStatus(UplaodPaper uplaodPaper,Integer statusSeq);

    List<UploadPaperQuestion> findByUplaodPaperAndStatusOrderByQuestionKeyAsc(UplaodPaper uplaodPaper,Integer statusSeq);

    UploadPaperQuestion findByUploadPaperQuestionSeq(Integer uplaodPaperQuestionSeq);
}
