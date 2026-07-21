package com.examflow.backend.entity;

import java.time.LocalDateTime;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tutor_instructor")
public class TutorInstructor {

    private Integer tutorInstructorSeq;

    private Tutor tutor;

    private Instructor instructor;

    private Integer tutorStatus;

    private Integer instructorStatus;

    private Boolean isEngaged;

    private LocalDateTime createdDateTime;

    private LocalDateTime lastModifiedDateTime;

    private String createdBy;

    private String lastModifiedBy;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "tutorInstructorSeq")
    public Integer getTutorInstructorSeq() {
        return tutorInstructorSeq;
    }

    public void setTutorInstructorSeq(Integer tutorInstructorSeq) {
        this.tutorInstructorSeq = tutorInstructorSeq;
    }

    @ManyToOne
    @JoinColumn(name = "tutorSeq")

    public Tutor getTutor() {
        return tutor;
    }

    public void setTutor(Tutor tutor) {
        this.tutor = tutor;
    }

    @ManyToOne
    @JoinColumn(name = "instructorSeq")
    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    @Column(name = "tutorStatus")
    public Integer getTutorStatus() {
        return tutorStatus;
    }

    public void setTutorStatus(Integer tutorStatus) {
        this.tutorStatus = tutorStatus;
    }

    @Column(name = "instructorStatus")
    public Integer getInstructorStatus() {
        return instructorStatus;
    }

    public void setInstructorStatus(Integer instructorStatus) {
        this.instructorStatus = instructorStatus;
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

    @Column(name = "isEngaged")
    public Boolean getIsEngaged() {
        return isEngaged;
    }

    public void setIsEngaged(Boolean isEngaged) {
        this.isEngaged = isEngaged;
    }

}
