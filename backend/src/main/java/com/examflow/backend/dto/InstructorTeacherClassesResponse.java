package com.examflow.backend.dto;

public class InstructorTeacherClassesResponse {
    

    private String id;


    private String class_name;

    private String class_year;

    private String subject;
    private String monthly_fee;
    private String status;
    

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClass_name() {
        return class_name;
    }
    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
    public String getClass_year() {
        return class_year;
    }
    public void setClass_year(String class_year) {
        this.class_year = class_year;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getMonthly_fee() {
        return monthly_fee;
    }
    public void setMonthly_fee(String monthly_fee) {
        this.monthly_fee = monthly_fee;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


    

}
