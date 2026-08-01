package com.examflow.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "paper_submission")
public class PaperSubmission {

	private Integer paperSubmissionSeq;

	private Student student;

	private UplaodPaper uplaodpaper;

	private String submissionFilePath;

	private Integer statusSeq;

	private LocalDateTime submissionDate;

	private String submissionBy;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "paper_submission_seq")
	public Integer getPaperSubmissionSeq() {
		return paperSubmissionSeq;
	}

	public void setPaperSubmissionSeq(Integer paperSubmissionSeq) {
		this.paperSubmissionSeq = paperSubmissionSeq;
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
	@JoinColumn(name = "upload_paper_seq", referencedColumnName = "upload_paper_seq")
	public UplaodPaper getUplaodpaper() {
		return uplaodpaper;
	}

	public void setUplaodpaper(UplaodPaper uplaodpaper) {
		this.uplaodpaper = uplaodpaper;
	}

	@Column(name = "submissionFilePath")
	public String getSubmissionFilePath() {
		return submissionFilePath;
	}

	public void setSubmissionFilePath(String submissionFilePath) {
		this.submissionFilePath = submissionFilePath;
	}

	@Column(name = "statusSeq")
	public Integer getStatusSeq() {
		return statusSeq;
	}

	public void setStatusSeq(Integer statusSeq) {
		this.statusSeq = statusSeq;
	}

	@Column(name = "submission_date")
	public LocalDateTime getSubmissionDate() {
		return submissionDate;
	}

	public void setSubmissionDate(LocalDateTime submissionDate) {
		this.submissionDate = submissionDate;
	}

	@Column(name = "submissionBy")
	public String getSubmissionBy() {
		return submissionBy;
	}

	public void setSubmissionBy(String submissionBy) {
		this.submissionBy = submissionBy;
	}

}
