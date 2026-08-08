package com.examflow.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SubmitGradeResponse {
    
    private Integer submissionId;

    private Integer teacherId;


    private Integer paperId;


    private Integer totalMarks;

    private Integer maxMarks;


    private String grade;

    private LocalDateTime gradedAt;

    private List<SubmitGradeQuestionsResponse> questions;

    public Integer getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Integer getMaxMarks() {
        return maxMarks;
    }

    public void setMaxMarks(Integer maxMarks) {
        this.maxMarks = maxMarks;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public LocalDateTime getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    public List<SubmitGradeQuestionsResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SubmitGradeQuestionsResponse> questions) {
        this.questions = questions;
    }





    

}
