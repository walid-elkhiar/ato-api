package com.ato.backendapi.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendPasswordEmail(String to, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("elkhiar.walid@gmail.com");
        message.setTo(to);
        message.setSubject("Your New Password");
        message.setText("Your new password is: " + password);
        javaMailSender.send(message);
    }

    // Nouvelle méthode pour envoyer un email de notification
    public void sendNotificationEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("elkhiar.walid@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }
}

