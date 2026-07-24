package com.examflow.backend.dto;

import java.time.LocalDateTime;

public class PaymentsListResponse {

    private Integer id;

    private Integer student_id;

    private String student_name;

    private String student_number;

    private String teacher_name;

    private String class_name;

    private Double amount;

    private String receipt_url;

    private String status;

    private String reference_number;

    private String rejection_reason;

    private LocalDateTime submitted_at;

    private LocalDateTime reviewed_at;

    private String reviewed_by;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public void setStudent_id(Integer student_id) {
        this.student_id = student_id;
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

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getReceipt_url() {
        return receipt_url;
    }

    public void setReceipt_url(String receipt_url) {
        this.receipt_url = receipt_url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReference_number() {
        return reference_number;
    }

    public void setReference_number(String reference_number) {
        this.reference_number = reference_number;
    }

    public String getRejection_reason() {
        return rejection_reason;
    }

    public void setRejection_reason(String rejection_reason) {
        this.rejection_reason = rejection_reason;
    }

    public LocalDateTime getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(LocalDateTime submitted_at) {
        this.submitted_at = submitted_at;
    }

    public LocalDateTime getReviewed_at() {
        return reviewed_at;
    }

    public void setReviewed_at(LocalDateTime reviewed_at) {
        this.reviewed_at = reviewed_at;
    }

    public String getReviewed_by() {
        return reviewed_by;
    }

    public void setReviewed_by(String reviewed_by) {
        this.reviewed_by = reviewed_by;
    }

}
