package com.examflow.backend.dto;

public class PaperUploadRequest {

    private String paper_name;

    private String status;

    private Integer paper_id;

    private Integer number_of_questions;

    private Integer year;

    private Integer month;

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPaper_id() {
        return paper_id;
    }

    public void setPaper_id(Integer paper_id) {
        this.paper_id = paper_id;
    }

    public Integer getNumber_of_questions() {
        return number_of_questions;
    }

    public void setNumber_of_questions(Integer number_of_questions) {
        this.number_of_questions = number_of_questions;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

}
