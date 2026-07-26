package com.examflow.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "noconfiguration")
public class NoConfiguration {

    private Integer noConfigSeq;

    private String noConfigName;

    private Integer noConfigValue;

    private String suffix;

    private Integer nextNumber;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "noConfigSeq")
    public Integer getNoConfigSeq() {
        return noConfigSeq;
    }

    public void setNoConfigSeq(Integer noConfigSeq) {
        this.noConfigSeq = noConfigSeq;
    }

    @Column(name = "noConfigName")
    public String getNoConfigName() {
        return noConfigName;
    }

    public void setNoConfigName(String noConfigName) {
        this.noConfigName = noConfigName;
    }

    @Column(name = "noConfigValue")
    public Integer getNoConfigValue() {
        return noConfigValue;
    }

    public void setNoConfigValue(Integer noConfigValue) {
        this.noConfigValue = noConfigValue;
    }


    @Column(name = "nextNumber")
    public Integer getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(Integer nextNumber) {
        this.nextNumber = nextNumber;
    }

    @Column(name = "suffix")
    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

}
