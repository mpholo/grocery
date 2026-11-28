package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Sends the account-lifecycle emails (signup received, superadmin notification,
 * activation link, password-reset link). All sends are best-effort: a mail
 * outage is logged, never thrown, so it can't break signup/approval.
 */
@Service
@Slf4j
public class AccountMailService {

    private final JavaMailSender mailSender;
    private final String from;
    private final String publicUrl;

    public AccountMailService(JavaMailSender mailSender,
                              @Value("${spring.mail.username:}") String from,
                              @Value("${app.public-url:https://grocery.dliving.co.za}") String publicUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.publicUrl = publicUrl.endsWith("/") ? publicUrl.substring(0, publicUrl.length() - 1) : publicUrl;
    }

    /** Sent to the user right after signup — their account awaits approval. */
    public void signupReceived(User user) {
        send(user.getEmail(), "Grocery — signup received",
                "Hi " + safeName(user) + ",\n\n"
                        + "Thanks for signing up for Grocery. Your request has been received and is "
                        + "waiting for an administrator to approve it. Once approved, we'll email you a "
                        + "link to set your username and password and activate your account.\n\n"
                        + "— Grocery");
    }

    /** Sent to each superadmin so they can approve the pending signup. */
    public void notifyPendingApproval(List<String> adminEmails, User user) {
        for (String admin : adminEmails) {
            send(admin, "Grocery — new signup awaiting approval",
                    "A new account is awaiting approval:\n\n"
                            + "Name:  " + safeName(user) + "\n"
                            + "Email: " + user.getEmail() + "\n\n"
                            + "Approve or reject it from the Grocery app (Pending users).");
        }
    }

    /** Sent on approval — the link where the user sets a username + password. */
    public void accountSetupLink(User user, String token) {
        String link = publicUrl + "/activate?token=" + token;
        send(user.getEmail(), "Grocery — set up your account",
                "Hi " + safeName(user) + ",\n\n"
                        + "Your Grocery account has been approved. Use the link below to choose your "
                        + "username and password and activate your account (valid for 7 days):\n\n"
                        + link + "\n\n"
                        + "— Grocery");
    }

    /** Sent on forgot-password — the link where the user sets a new password. */
    public void passwordResetLink(User user, String token) {
        String link = publicUrl + "/reset-password?token=" + token;
        send(user.getEmail(), "Grocery — reset your password",
                "Hi " + safeName(user) + ",\n\n"
                        + "Use the link below to set a new password (valid for 1 hour). If you didn't "
                        + "request this, you can ignore this email.\n\n"
                        + link + "\n\n"
                        + "— Grocery");
    }

    private String safeName(User user) {
        String n = user.getFirstName();
        return (n == null || n.isEmpty()) ? "there" : n;
    }

    private void send(String to, String subject, String body) {
        if (to == null || to.isEmpty()) {
            return;
        }
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            if (from != null && !from.isEmpty()) {
                msg.setFrom(from);
            }
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            log.info("Sent '{}' email to {}", subject, to);
        } catch (Exception e) {
            log.warn("Failed to send '{}' email to {}: {}", subject, to, e.getMessage());
        }
    }
}
