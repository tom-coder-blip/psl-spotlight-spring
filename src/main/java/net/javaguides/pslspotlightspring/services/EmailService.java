package net.javaguides.pslspotlightspring.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${client.url}")
    private String clientUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "Welcome to PSL Spotlight!";
        String html = """
            <h2>Hi %s,</h2>
            <p>Welcome to PSL Spotlight 🎉</p>
            <p>Your account has been created successfully. Start posting, liking, and commenting to boost your favorite players' trending ratings!</p>
            <p>Cheers,<br/>The PSL Spotlight Team</p>
        """.formatted(username);

        sendHtmlEmail(to, subject, html);
    }

    public void sendPasswordResetEmail(String to, String resetToken) {
        String resetUrl = clientUrl + "/reset-password/" + resetToken;
        String subject = "Password Reset Request";
        String html = """
            <h2>Password Reset</h2>
            <p>You requested a password reset. Click the link below to reset your password:</p>
            <a href="%s">%s</a>
            <p>If you did not request this, please ignore this email.</p>
        """.formatted(resetUrl, resetUrl);

        sendHtmlEmail(to, subject, html);
    }

    private void sendHtmlEmail(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
            System.out.println("Email sent to " + to);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
        }
    }
}