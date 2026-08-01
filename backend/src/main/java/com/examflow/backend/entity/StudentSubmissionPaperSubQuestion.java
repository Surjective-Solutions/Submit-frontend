package com.examflow.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "student_submission_paper_sub_question")
public class StudentSubmissionPaperSubQuestion {
    

     private Integer studentSubmissionPaperSubQuestionSeq;

     private StudentSubmissionPaperQuestion studentSubmissionPaperQuestion;

     private UploadPaperQuestionSubQuestion uploadPaperQuestionSubQuestion;

     private Integer statusSeq;


    @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentSubmissionPaperSubQuestionSeq")
     public Integer getStudentSubmissionPaperSubQuestionSeq() {
         return studentSubmissionPaperSubQuestionSeq;
     }

     public void setStudentSubmissionPaperSubQuestionSeq(Integer studentSubmissionPaperSubQuestionSeq) {
         this.studentSubmissionPaperSubQuestionSeq = studentSubmissionPaperSubQuestionSeq;
     }

     @ManyToOne
    @JoinColumn(name = "studentSubmissionPaperQuestionSeq", referencedColumnName = "studentSubmissionPaperQuestionSeq") 
     public StudentSubmissionPaperQuestion getStudentSubmissionPaperQuestion() {
         return studentSubmissionPaperQuestion;
     }

     public void setStudentSubmissionPaperQuestion(StudentSubmissionPaperQuestion studentSubmissionPaperQuestion) {
         this.studentSubmissionPaperQuestion = studentSubmissionPaperQuestion;
     }
@Column(name = "statusSeq")
     public Integer getStatusSeq() {
         return statusSeq;
     }

     public void setStatusSeq(Integer statusSeq) {
         this.statusSeq = statusSeq;
     }


          @ManyToOne
    @JoinColumn(name = "uploadPaperQuestionSubQuestionSeq", referencedColumnName = "uploadPaperQuestionSubQuestionSeq") 
     public UploadPaperQuestionSubQuestion getUploadPaperQuestionSubQuestion() {
         return uploadPaperQuestionSubQuestion;
     }

     public void setUploadPaperQuestionSubQuestion(UploadPaperQuestionSubQuestion uploadPaperQuestionSubQuestion) {
         this.uploadPaperQuestionSubQuestion = uploadPaperQuestionSubQuestion;
     }


     

     
}
