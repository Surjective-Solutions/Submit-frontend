package com.examflow.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "email_structure")
public class EmailStructure {
    
    private Integer emailStructureSeq;

    private String emialBody;

    private String emailName;

    private String subject;

    private Integer statusSeq;


@Id
 @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
 @Column(name = "emailStructureSeq")
    public Integer getEmailStructureSeq() {
        return emailStructureSeq;
    }

    public void setEmailStructureSeq(Integer emailStructureSeq) {
        this.emailStructureSeq = emailStructureSeq;
    }

    @Column(name = "emialBody")
    public String getEmialBody() {
        return emialBody;
    }

    public void setEmialBody(String emialBody) {
        this.emialBody = emialBody;
    }
@Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Column(name = "statusSeq")
    public Integer getStatusSeq() {
        return statusSeq;
    }

    public void setStatusSeq(Integer statusSeq) {
        this.statusSeq = statusSeq;
    }

    
    @Column(name = "emailName")
    public String getEmailName() {
        return emailName;
    }

    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

    




    
}
