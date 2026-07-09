package com.examflow.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "upload_paper_Question_subQuestion")
public class UploadPaperQuestionSubQuestion {

    private Integer uploadPaperQuestionSubQuestionSeq;

    private Integer questionKey;

    private Integer mark;

    private Integer status;

    private UploadPaperQuestion uploadPaperQuestion;

    private LocalDateTime createdDateTime;

    private LocalDateTime lastModifiedDateTime;

    private String createdBy;

    private String lastModifiedBy;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "uploadPaperQuestionSubQuestionSeq")
    public Integer getUploadPaperQuestionSubQuestionSeq() {
        return uploadPaperQuestionSubQuestionSeq;
    }

    public void setUploadPaperQuestionSubQuestionSeq(Integer uploadPaperQuestionSubQuestionSeq) {
        this.uploadPaperQuestionSubQuestionSeq = uploadPaperQuestionSubQuestionSeq;
    }

    @Column(name = "questionKey")
    public Integer getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(Integer questionKey) {
        this.questionKey = questionKey;
    }

    @Column(name = "mark")
    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "uploadPaperQuestionSeq", nullable = true)
    public UploadPaperQuestion getUploadPaperQuestion() {
        return uploadPaperQuestion;
    }

    public void setUploadPaperQuestion(UploadPaperQuestion uploadPaperQuestion) {
        this.uploadPaperQuestion = uploadPaperQuestion;
    }

    @Column(name = "createdDateTime")
    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Column(name = "lastModifiedDateTime")
    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    @Column(name = "createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "lastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}
