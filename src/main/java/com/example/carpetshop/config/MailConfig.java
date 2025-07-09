package com.example.carpetshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        System.out.println("🔧 Configuring JavaMailSender for Gmail...");
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("carpetshop.reply@gmail.com");
        
        // Get Gmail App Password from environment variable
        String appPassword = System.getenv("GMAIL_APP_PASSWORD");
        if (appPassword == null || appPassword.isEmpty()) {
            System.err.println("❌ GMAIL_APP_PASSWORD environment variable is not set!");
            throw new RuntimeException("GMAIL_APP_PASSWORD environment variable is required");
        }
        
        System.out.println("🔧 Gmail App Password exists: " + (appPassword != null && !appPassword.isEmpty()));
        mailSender.setPassword(appPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        System.out.println("🔧 JavaMailSender configured successfully for Gmail!");
        return mailSender;
    }
} 