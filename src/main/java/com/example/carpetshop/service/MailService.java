package com.example.carpetshop.service;


import org.springframework.beans.factory.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${carpetshop.mail.from}")
    private String from;

    public void sendOrderConfirmationEmail(String to, String subject, String content) {
        try {
            System.out.println("📧 Attempting to send email to: " + to);
            System.out.println("📧 From: " + from);
            System.out.println("📧 Subject: " + subject);
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            
            System.out.println("✅ Email sent successfully!");
        } catch (Exception e) {
            System.err.println("❌ Email sending failed: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw để OrderService có thể handle
        }
    }
}
