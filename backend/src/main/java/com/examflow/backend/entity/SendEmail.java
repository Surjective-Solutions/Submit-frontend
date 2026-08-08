package com.examflow.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "sendemail")
public class SendEmail {
    
    private Integer senderEmailSeq;

    private String emailName;

    private String sendTo;

    private String emailSubject;

    private String emailBody;

    private LocalDateTime sendTime;


    private Integer statusSeq;

     private String errorMsg;



    @Id
     @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
 @Column(name = "senderEmailSeq")
    public Integer getSenderEmailSeq() {
        return senderEmailSeq;
    }


    public void setSenderEmailSeq(Integer senderEmailSeq) {
        this.senderEmailSeq = senderEmailSeq;
    }

 @Column(name = "emailName")
    public String getEmailName() {
        return emailName;
    }


    public void setEmailName(String emailName) {
        this.emailName = emailName;
    }

 @Column(name = "sendTo")
    public String getSendTo() {
        return sendTo;
    }


    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

 @Column(name = "emailSubject")
    public String getEmailSubject() {
        return emailSubject;
    }


    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

 @Column(name = "emailBody")
    public String getEmailBody() {
        return emailBody;
    }


    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

 @Column(name = "sendTime")
    public LocalDateTime getSendTime() {
        return sendTime;
    }


    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

 @Column(name = "statusSeq")
    public Integer getStatusSeq() {
        return statusSeq;
    }


    public void setStatusSeq(Integer statusSeq) {
        this.statusSeq = statusSeq;
    }

 @Column(name = "errorMsg")
    public String getErrorMsg() {
        return errorMsg;
    }


    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    


    

}
