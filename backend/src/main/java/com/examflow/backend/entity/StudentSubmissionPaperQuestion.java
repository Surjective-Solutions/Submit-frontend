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
@Table(name = "student_submission_paper_question")
public class StudentSubmissionPaperQuestion {
    
    private Integer studentSubmissionPaperQuestionSeq;

    private Student student;

    private UploadPaperQuestion uploadPaperQuestion;

    private Integer statusSeq;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "studentSubmissionPaperQuestionSeq")
    public Integer getStudentSubmissionPaperQuestionSeq() {
        return studentSubmissionPaperQuestionSeq;
    }

    public void setStudentSubmissionPaperQuestionSeq(Integer studentSubmissionPaperQuestionSeq) {
        this.studentSubmissionPaperQuestionSeq = studentSubmissionPaperQuestionSeq;
    }

    @ManyToOne
    @JoinColumn(name = "student_seq", referencedColumnName = "student_seq")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

      @ManyToOne
    @JoinColumn(name = "uploadPaperQuestionSeq", referencedColumnName = "uploadPaperQuestionSeq")
    public UploadPaperQuestion getUploadPaperQuestion() {
        return uploadPaperQuestion;
    }

    public void setUploadPaperQuestion(UploadPaperQuestion uploadPaperQuestion) {
        this.uploadPaperQuestion = uploadPaperQuestion;
    }
@Column(name = "statusSeq")
    public Integer getStatusSeq() {
        return statusSeq;
    }

    public void setStatusSeq(Integer statusSeq) {
        this.statusSeq = statusSeq;
    }


    
}
