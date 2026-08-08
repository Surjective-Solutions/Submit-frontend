package com.examflow.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PaperInstructorTutorResponse {
    
    private String id;

    private String paper_name;

    private String class_name;
    private String class_id;
    private String month_label;
    private String number_of_questions;

    private LocalDateTime uploaded_at;

    private List<QuestionPaperInstructorTutorResponse> questions;

    private List<SubmissionPaperInstructorTutorResponse> submissions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaper_name() {
        return paper_name;
    }

    public void setPaper_name(String paper_name) {
        this.paper_name = paper_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getMonth_label() {
        return month_label;
    }

    public void setMonth_label(String month_label) {
        this.month_label = month_label;
    }

    public String getNumber_of_questions() {
        return number_of_questions;
    }

    public void setNumber_of_questions(String number_of_questions) {
        this.number_of_questions = number_of_questions;
    }

    public LocalDateTime getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(LocalDateTime uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public List<QuestionPaperInstructorTutorResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionPaperInstructorTutorResponse> questions) {
        this.questions = questions;
    }

    public List<SubmissionPaperInstructorTutorResponse> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SubmissionPaperInstructorTutorResponse> submissions) {
        this.submissions = submissions;
    }



    

}
