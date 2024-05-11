package com.project.budgoal.services;

import com.project.budgoal.entites.Users;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class EmailService {
    
    private final JavaMailSender javaMailSender;


    @SneakyThrows
    public void sendEmail(Users users) {


        String subject = "Please verify your email";
        String body = "<p> Dear " + users.getFirstName() + " " + users.getLastName() + ",</p>";
        body += "<p>Please click the link below to verify your email address</p>";
        String link = "http://localhost:8080/budgoal/auth/confirm-email-address?token=";
        body += "<p>Thank you,<br> Budgoal<p>";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            String sender = "buddggoall@gmail.com";
            helper.setFrom(sender);
            helper.setTo(users.getEmail());
            helper.setSubject(subject);
            helper.setText(body, true); // Set HTML to true for proper rendering
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Handle exception
            e.printStackTrace();
        }


    }
}
