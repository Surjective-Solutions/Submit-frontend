package com.examflow.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SubmissionPaperInstructorTutorResponse {
    private String id;

     private String student_name;

     private String student_number;

     private LocalDateTime submitted_at;
     
    private LocalDateTime graded_at;

     private boolean graded;

     private String grade;

     private String graded_by;

     private String file_url;

     private List<QuestionGradeResponse> awarded_marks;

    

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
     public String getGrade() {
         return grade;
     }
     public void setGrade(String grade) {
         this.grade = grade;
     }
     public String getGraded_by() {
         return graded_by;
     }
     public void setGraded_by(String graded_by) {
         this.graded_by = graded_by;
     }
     public List<QuestionGradeResponse> getAwarded_marks() {
         return awarded_marks;
     }
     public void setAwarded_marks(List<QuestionGradeResponse> awarded_marks) {
         this.awarded_marks = awarded_marks;
     }


     
     
     
     

}
