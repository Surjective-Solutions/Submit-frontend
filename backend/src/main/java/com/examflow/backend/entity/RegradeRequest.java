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
@Table(name = "regrade_request")
public class RegradeRequest {

    private Integer regradeRequestSeq;

    private PaperSubmission paperSubmission;

    private GradeSubmission gradeSubmission;

    private String reason;

    private Integer status;

    private LocalDateTime requestedAt;

    private String requestedBy;

    private LocalDateTime resolvedAt;

    private String resolvedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "regrade_request_seq")
    public Integer getRegradeRequestSeq() {
        return regradeRequestSeq;
    }

    public void setRegradeRequestSeq(Integer regradeRequestSeq) {
        this.regradeRequestSeq = regradeRequestSeq;
    }

    @ManyToOne
    @JoinColumn(name = "paper_submission_seq", nullable = false)
    public PaperSubmission getPaperSubmission() {
        return paperSubmission;
    }

    public void setPaperSubmission(PaperSubmission paperSubmission) {
        this.paperSubmission = paperSubmission;
    }

    // Snapshot of exactly which grading record (marks) this request was raised
    // against, taken at request time. Grade edits now supersede the old
    // GradeSubmission with a brand new row rather than mutating it in place, so
    // resolving "previous marks" via a live lookup would break once the regrade
    // completes - this FK keeps the request identifiable by its marks even after.
    @ManyToOne
    @JoinColumn(name = "grade_submission_seq", nullable = false)
    public GradeSubmission getGradeSubmission() {
        return gradeSubmission;
    }

    public void setGradeSubmission(GradeSubmission gradeSubmission) {
        this.gradeSubmission = gradeSubmission;
    }

    @Column(name = "reason", columnDefinition = "TEXT")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    // 1 = pending / open, 2 = completed - matches the same status-flag
    // convention used by GradeSubmission / GradeSubmissionQuestion elsewhere.
    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "requested_at")
    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    @Column(name = "requested_by")
    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    @Column(name = "resolved_at")
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    @Column(name = "resolved_by")
    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
}
