package com.examflow.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "grade_submission")
public class GradeSubmission {
    private Integer gradeSubmissionSeq;

    private PaperSubmission paperSubmission;

    private Integer totalMarks;

    private Integer maxMarks;

    private String grade;

    private Integer status;

    private LocalDateTime gradedAt;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private String grardedBy;

    private String createdBy;
    private String lastModifiedBy;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "grade_submission_seq")
    public Integer getGradeSubmissionSeq() {
        return gradeSubmissionSeq;
    }


    public void setGradeSubmissionSeq(Integer gradeSubmissionSeq) {
        this.gradeSubmissionSeq = gradeSubmissionSeq;
    }







    @Column(name = "total_marks")
    public Integer getTotalMarks() {
        return totalMarks;
    }
    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }



    @Column(name = "max_marks")
    public Integer getMaxMarks() {
        return maxMarks;
    }
    public void setMaxMarks(Integer maxMarks) {
        this.maxMarks = maxMarks;
    }



    @Column(name = "grade")
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }



    @Column(name = "graded_at")
    public LocalDateTime getGradedAt() {
        return gradedAt;
    }
    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }




    @Column(name = "created_at")
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }




    @Column(name = "last_modified_at")
    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }




    @Column(name = "graded_by")
    public String getGrardedBy() {
        return grardedBy;
    }
    public void setGrardedBy(String grardedBy) {
        this.grardedBy = grardedBy;
    }




    @Column(name = "created_by")
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }



    @Column(name = "last_modified_by")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }



    @ManyToOne
    @JoinColumn(name = "paper_submission_seq", nullable = false)
    public PaperSubmission getPaperSubmission() {
        return paperSubmission;
    }


    public void setPaperSubmission(PaperSubmission paperSubmission) {
        this.paperSubmission = paperSubmission;
    }


    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }


    public void setStatus(Integer status) {
        this.status = status;
    }


    

    


    
}
