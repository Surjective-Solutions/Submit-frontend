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
@Table(name = "uplaod_paper_question")
public class UploadPaperQuestion {

    private Integer uploadPaperQuestionSeq;

    private UplaodPaper uplaodPaper;

    private Integer marks;

    private Integer questionKey;

    private Integer status;

    private LocalDateTime createdDateTime;

    private LocalDateTime lastModifiedDateTime;

    private String createdBy;

    private String lastModifiedBy;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "uploadPaperQuestionSeq")
    public Integer getUploadPaperQuestionSeq() {
        return uploadPaperQuestionSeq;
    }

    public void setUploadPaperQuestionSeq(Integer uploadPaperQuestionSeq) {
        this.uploadPaperQuestionSeq = uploadPaperQuestionSeq;
    }

    @ManyToOne
    @JoinColumn(name = "upload_paper_seq", nullable = true)
    public UplaodPaper getUplaodPaper() {
        return uplaodPaper;
    }

    public void setUplaodPaper(UplaodPaper uplaodPaper) {
        this.uplaodPaper = uplaodPaper;
    }

    @Column(name = "marks")
    public Integer getMarks() {
        return marks;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    @Column(name = "questionKey")
    public Integer getQuestionKey() {
        return questionKey;
    }

    public void setQuestionKey(Integer questionKey) {
        this.questionKey = questionKey;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
