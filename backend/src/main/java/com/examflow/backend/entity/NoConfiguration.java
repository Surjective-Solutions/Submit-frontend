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

    private String Suffix;

    private Integer nextNumber;

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "no_config_seq")
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

    @Column(name = "Suffix")
    public String getSuffix() {
        return Suffix;
    }

    public void setSuffix(String suffix) {
        Suffix = suffix;
    }

    @Column(name = "nextNumber")
    public Integer getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(Integer nextNumber) {
        this.nextNumber = nextNumber;
    }

}
