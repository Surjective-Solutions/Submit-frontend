package com.examflow.backend.dto;

public class ClassRequest {

    private String description;
    private String display_name;
    private String subject_name;
    private Double monthly_fee;
    private Integer classSeq;

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

    public Double getMonthly_fee() {
        return monthly_fee;
    }

    public void setMonthly_fee(Double monthly_fee) {
        this.monthly_fee = monthly_fee;
    }

    public Integer getClassSeq() {
        return classSeq;
    }

    public void setClassSeq(Integer classSeq) {
        this.classSeq = classSeq;
    }

}
