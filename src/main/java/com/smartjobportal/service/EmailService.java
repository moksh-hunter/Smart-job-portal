package com.smartjobportal.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.base-url}")
    private String baseUrl;

    @Async
    public void sendEmailVerification(String toEmail, String token) {
        String subject = "Verify Your Email - Smart Job Portal";
        String verificationLink = baseUrl + "/api/v1/auth/verify-email?token=" + token;
        String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 30px; border-radius: 10px;">
                        <h2 style="color: #2c3e50;">Welcome to Smart Job Portal!</h2>
                        <p>Thank you for registering. Please verify your email address by clicking the button below:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background-color: #3498db; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-size: 16px;">Verify Email</a>
                        </div>
                        <p style="color: #7f8c8d;">This link will expire in 24 hours.</p>
                        <p style="color: #7f8c8d;">If you didn't create an account, please ignore this email.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="color: #bdc3c7; font-size: 12px;">Smart Job Portal Team</p>
                    </div>
                </body>
                </html>
                """.formatted(verificationLink);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        String subject = "Reset Your Password - Smart Job Portal";
        String resetLink = baseUrl + "/api/v1/auth/reset-password?token=" + token;
        String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 30px; border-radius: 10px;">
                        <h2 style="color: #2c3e50;">Password Reset Request</h2>
                        <p>We received a request to reset your password. Click the button below to create a new password:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background-color: #e74c3c; color: white; padding: 12px 30px; text-decoration: none; border-radius: 5px; font-size: 16px;">Reset Password</a>
                        </div>
                        <p style="color: #7f8c8d;">This link will expire in 1 hour.</p>
                        <p style="color: #7f8c8d;">If you didn't request a password reset, please ignore this email.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="color: #bdc3c7; font-size: 12px;">Smart Job Portal Team</p>
                    </div>
                </body>
                </html>
                """.formatted(resetLink);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendApplicationStatusEmail(String toEmail, String candidateName, String jobTitle, String status) {
        String subject = "Application Status Update - Smart Job Portal";
        String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 30px; border-radius: 10px;">
                        <h2 style="color: #2c3e50;">Application Status Update</h2>
                        <p>Dear %s,</p>
                        <p>Your application for <strong>%s</strong> has been updated to: <strong>%s</strong></p>
                        <p>Log in to your account for more details.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="color: #bdc3c7; font-size: 12px;">Smart Job Portal Team</p>
                    </div>
                </body>
                </html>
                """.formatted(candidateName, jobTitle, status);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    @Async
    public void sendInterviewScheduledEmail(String toEmail, String candidateName, String jobTitle,
                                             String date, String time, String round) {
        String subject = "Interview Scheduled - Smart Job Portal";
        String htmlContent = """
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px;">
                    <div style="max-width: 600px; margin: 0 auto; background: #f9f9f9; padding: 30px; border-radius: 10px;">
                        <h2 style="color: #2c3e50;">Interview Scheduled</h2>
                        <p>Dear %s,</p>
                        <p>Your interview for <strong>%s</strong> has been scheduled:</p>
                        <ul>
                            <li><strong>Date:</strong> %s</li>
                            <li><strong>Time:</strong> %s</li>
                            <li><strong>Round:</strong> %s</li>
                        </ul>
                        <p>Please be prepared and on time. Good luck!</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                        <p style="color: #bdc3c7; font-size: 12px;">Smart Job Portal Team</p>
                    </div>
                </body>
                </html>
                """.formatted(candidateName, jobTitle, date, time, round);

        sendHtmlEmail(toEmail, subject, htmlContent);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
