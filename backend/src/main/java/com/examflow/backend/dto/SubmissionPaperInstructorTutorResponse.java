package com.examflow.backend.dto;

import java.time.LocalDateTime;

public class SubmissionPaperInstructorTutorResponse {
    private String id;

     private String student_name;

     private String student_number;

     private LocalDateTime submitted_at;
     
    private LocalDateTime graded_at;

     private boolean graded;

     private String file_url;

     public String getId() {
         return id;
     }
     public void setId(String id) {
         this.id = id;
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
     public LocalDateTime getSubmitted_at() {
         return submitted_at;
     }
     public void setSubmitted_at(LocalDateTime submitted_at) {
         this.submitted_at = submitted_at;
     }
     public LocalDateTime getGraded_at() {
         return graded_at;
     }
     public void setGraded_at(LocalDateTime graded_at) {
         this.graded_at = graded_at;
     }
     public boolean isGraded() {
         return graded;
     }
     public void setGraded(boolean graded) {
         this.graded = graded;
     }
     public String getFile_url() {
         return file_url;
     }
     public void setFile_url(String file_url) {
         this.file_url = file_url;
     }


     
     
     

}
