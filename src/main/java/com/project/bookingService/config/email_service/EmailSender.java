package com.project.bookingService.config.email_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSender {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(RecipientDTO recipient) {
        try {
            SimpleMailMessage mailMessage =
                    new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(recipient.getRecipient());
            mailMessage.setText(recipient.getMessageBody());
            mailMessage.setSubject(recipient.getSubject());

            javaMailSender.send(mailMessage);
            return "Email has been sent successfully.";

        } catch (Exception e) {
            return "Failed sending email: " + e.getMessage();
        }
    }
}
