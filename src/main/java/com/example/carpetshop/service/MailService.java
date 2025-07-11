package com.example.carpetshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOrderConfirmationEmail(String to, String subject, String content) {
        try {
            System.out.println("Sending email to: " + to);
            
            // Get from email from environment variable
            String fromEmail = System.getenv("GMAIL_FROM_EMAIL");
            if (fromEmail == null || fromEmail.isEmpty()) {
                fromEmail = "carpetshop.reply@gmail.com"; // fallback
            }
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
            
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
