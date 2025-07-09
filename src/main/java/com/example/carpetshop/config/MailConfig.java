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
        System.out.println("🔧 Configuring JavaMailSender for SendGrid...");
        
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.sendgrid.net");
        mailSender.setPort(587);
        mailSender.setUsername("apikey");
        
        // Get API key from environment variable
        String apiKey = System.getenv("SENDGRID_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("❌ SENDGRID_API_KEY environment variable is not set!");
            throw new RuntimeException("SENDGRID_API_KEY environment variable is required");
        }
        
        System.out.println("🔧 API Key exists: " + (apiKey != null && !apiKey.isEmpty()));
        mailSender.setPassword(apiKey);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        System.out.println("🔧 JavaMailSender configured successfully!");
        return mailSender;
    }
} 