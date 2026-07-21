package com.examflow.backend.dto;

import java.time.LocalDateTime;

public class UploadPaperResponse {

    private Integer id;

    private String paper_name;

    private Integer month;

    private Integer year;

    private Integer number_of_questions;

    private LocalDateTime uploaded_at;

    private String pdf_url;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getNumber_of_questions() {
        return number_of_questions;
    }

    public void setNumber_of_questions(Integer number_of_questions) {
        this.number_of_questions = number_of_questions;
    }

    public LocalDateTime getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(LocalDateTime uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

}
