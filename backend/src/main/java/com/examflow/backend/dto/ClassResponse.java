package com.examflow.backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ClassResponse {

    private Integer id;
    private String description;
    private String display_name;
    private String subject_name;
    private String teacher_name;
    private LocalDateTime enrolled_at;
    private String class_name;
    private String subject;
    private Integer monthly_fee;
    private String status;
    private List<UploadPaperResponse> papers;
    private List<StudentClassPaymentRecordResponse> monthly_payments;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public Integer getMonthly_fee() {
        return monthly_fee;
    }

    public void setMonthly_fee(Integer monthly_fee) {
        this.monthly_fee = monthly_fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UploadPaperResponse> getPapers() {
        return papers;
    }

    public void setPapers(List<UploadPaperResponse> papers) {
        this.papers = papers;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public LocalDateTime getEnrolled_at() {
        return enrolled_at;
    }

    public void setEnrolled_at(LocalDateTime enrolled_at) {
        this.enrolled_at = enrolled_at;
    }

    public List<StudentClassPaymentRecordResponse> getMonthly_payments() {
        return monthly_payments;
    }

    public void setMonthly_payments(List<StudentClassPaymentRecordResponse> monthly_payments) {
        this.monthly_payments = monthly_payments;
    }

}
