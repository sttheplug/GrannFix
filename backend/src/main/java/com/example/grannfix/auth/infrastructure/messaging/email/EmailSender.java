package com.example.grannfix.auth.infrastructure.messaging.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
