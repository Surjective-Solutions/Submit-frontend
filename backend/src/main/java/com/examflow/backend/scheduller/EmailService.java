package com.examflow.backend.scheduller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.examflow.backend.entity.EmailStructure;
import com.examflow.backend.entity.SendEmail;
import com.examflow.backend.repository.EmailStructureRepository;
import com.examflow.backend.repository.SendEmailRepository;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


      @Autowired
    private SendEmailRepository sendEmailRepository;

    @Autowired
    private EmailStructureRepository emailStructureRepository;

    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }



        public void sendOTP(String to, Integer otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        EmailStructure emailStructure = emailStructureRepository.findByEmailNameAndStatusSeq("OTPCONFERMATION", 2);
        SendEmail sendEmail = new SendEmail();

        message.setTo(to);
        message.setSubject(emailStructure.getSubject());
        message.setText(emailStructure.getEmialBody() + otp);


        sendEmail.setEmailBody(emailStructure.getEmialBody() + otp);
        sendEmail.setEmailSubject(emailStructure.getSubject());
        sendEmail.setSendTo(to);
        sendEmail.setSendTime(LocalDateTime.now());
        sendEmail.setEmailName("OTPCONFERMATION");

        try {
            mailSender.send(message);
            sendEmail.setStatusSeq(2);
        } catch (MailException e ) {

            sendEmail.setStatusSeq(1);
            sendEmail.setErrorMsg(e.getMessage());
            
        }

        sendEmailRepository.save(sendEmail);




    }

    public void sendPaymentUnderReview(String to) {
        sendTemplatedEmail(to, "PAYMENT_UNDER_REVIEW", "");
    }

    public void sendPaymentApproved(String to) {
        sendTemplatedEmail(to, "PAYMENT_APPROVED", "");
    }

    public void sendPaymentRejected(String to, String reason) {
        sendTemplatedEmail(to, "PAYMENT_REJECTED", reason != null ? reason : "");
    }

    private void sendTemplatedEmail(String to, String emailName, String bodySuffix) {
        EmailStructure emailStructure = emailStructureRepository.findByEmailNameAndStatusSeq(emailName, 2);
        SendEmail sendEmail = new SendEmail();
        sendEmail.setSendTo(to);
        sendEmail.setSendTime(LocalDateTime.now());
        sendEmail.setEmailName(emailName);

        if (emailStructure == null) {
            sendEmail.setStatusSeq(1);
            sendEmail.setErrorMsg("No active email template found for '" + emailName + "'");
            sendEmailRepository.save(sendEmail);
            return;
        }

        String body = emailStructure.getEmialBody() + bodySuffix;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(emailStructure.getSubject());
        message.setText(body);

        sendEmail.setEmailBody(body);
        sendEmail.setEmailSubject(emailStructure.getSubject());

        try {
            mailSender.send(message);
            sendEmail.setStatusSeq(2);
        } catch (MailException e) {
            sendEmail.setStatusSeq(1);
            sendEmail.setErrorMsg(e.getMessage());
        }

        sendEmailRepository.save(sendEmail);
    }
}
