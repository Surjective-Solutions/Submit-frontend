package com.examflow.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "student_class_payment_record")
public class StudentClassPaymentRecord {

    private Integer studentClassPaymentRecordSeq;

    private ClassPaymentRecord classPaymentRecord;

    private Student student;

    private Boolean isPayed;

    private Boolean isApproved;

    private Integer status;

    private String reffrenceNo;

    private String reson;

    private Boolean isForPayments;

    private Double payedAmount;

    private LocalDateTime payedTime;

    private LocalDateTime approvedTime;

    private String reciptPath;

    private String payedBy;

    private String approvedBy;

    private LocalDateTime createdDateTime;

    private LocalDateTime lastModifiedDateTime;

    private String createdBy;

    private String lastModifiedBy;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "studentClassPaymentRecordSeq")
    public Integer getStudentClassPaymentRecordSeq() {
        return studentClassPaymentRecordSeq;
    }

    public void setStudentClassPaymentRecordSeq(Integer studentClassPaymentRecordSeq) {
        this.studentClassPaymentRecordSeq = studentClassPaymentRecordSeq;
    }

    @ManyToOne
    @JoinColumn(name = "classPaymentRecordSeq")
    public ClassPaymentRecord getClassPaymentRecord() {
        return classPaymentRecord;
    }

    public void setClassPaymentRecord(ClassPaymentRecord classPaymentRecord) {
        this.classPaymentRecord = classPaymentRecord;
    }

    @ManyToOne
    @JoinColumn(name = "student_seq")
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Column(name = "isPayed")
    public Boolean getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(Boolean isPayed) {
        this.isPayed = isPayed;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "reffrenceNo")
    public String getReffrenceNo() {
        return reffrenceNo;
    }

    public void setReffrenceNo(String reffrenceNo) {
        this.reffrenceNo = reffrenceNo;
    }

    @Column(name = "payedAmount")
    public Double getPayedAmount() {
        return payedAmount;
    }

    public void setPayedAmount(Double payedAmount) {
        this.payedAmount = payedAmount;
    }

    @Column(name = "payedTime")
    public LocalDateTime getPayedTime() {
        return payedTime;
    }


    public void setPayedTime(LocalDateTime payedTime) {
        this.payedTime = payedTime;
    }

    @Column(name = "approvedTime")
    public LocalDateTime getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(LocalDateTime approvedTime) {
        this.approvedTime = approvedTime;
    }

    @Column(name = "payedBy")
    public String getPayedBy() {
        return payedBy;
    }

    public void setPayedBy(String payedBy) {
        this.payedBy = payedBy;
    }

    @Column(name = "approvedBy")
    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Column(name = "createdDateTime")
    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    @Column(name = "lastModifiedDateTime")
    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(LocalDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    @Column(name = "createdBy")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "lastModifiedBy")
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Column(name = "reciptPath")
    public String getReciptPath() {
        return reciptPath;
    }

    public void setReciptPath(String reciptPath) {
        this.reciptPath = reciptPath;
    }

    @Column(name = "isApproved")
    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    @Column(name = "isForPayments")
    public Boolean getIsForPayments() {
        return isForPayments;
    }

    public void setIsForPayments(Boolean isForPayments) {
        this.isForPayments = isForPayments;
    }

    @Column(name = "reson")
    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

}
