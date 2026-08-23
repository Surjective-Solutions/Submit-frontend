package com.examflow.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RegradeRequestResponse {

    private Integer id;

    private Integer submission_id;

    private Integer paper_id;

    private String paper_name;

    private Integer class_id;

    private String class_name;

    private String student_name;

    private String student_number;

    private String reason;

    private LocalDateTime requested_at;

    private String status;

    private LocalDateTime resolved_at;

    private Integer previous_total_marks;

    private Integer previous_max_marks;

    private String previous_grade;

    private List<QuestionGradeResponse> previous_questions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubmission_id() {
        return submission_id;
    }

    public void setSubmission_id(Integer submission_id) {
        this.submission_id = submission_id;
    }

    public Integer getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(Integer paper_id) {
        this.paper_id = paper_id;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public Integer getClass_id() {
        return class_id;
    }

    public void setClass_id(Integer class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_number() {
        return student_number;
    }

    public void setStudent_number(String student_number) {
        this.student_number = student_number;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getRequested_at() {
        return requested_at;
    }

    public void setRequested_at(LocalDateTime requested_at) {
        this.requested_at = requested_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getResolved_at() {
        return resolved_at;
    }

    public void setResolved_at(LocalDateTime resolved_at) {
        this.resolved_at = resolved_at;
    }

    public Integer getPrevious_total_marks() {
        return previous_total_marks;
    }

    public void setPrevious_total_marks(Integer previous_total_marks) {
        this.previous_total_marks = previous_total_marks;
    }

    public Integer getPrevious_max_marks() {
        return previous_max_marks;
    }

    public void setPrevious_max_marks(Integer previous_max_marks) {
        this.previous_max_marks = previous_max_marks;
    }

    public String getPrevious_grade() {
        return previous_grade;
    }

    public void setPrevious_grade(String previous_grade) {
        this.previous_grade = previous_grade;
    }

    public List<QuestionGradeResponse> getPrevious_questions() {
        return previous_questions;
    }

    public void setPrevious_questions(List<QuestionGradeResponse> previous_questions) {
        this.previous_questions = previous_questions;
    }
}
