package com.example.grannfix.auth.email;

public interface EmailSender {
    void send(String to, String subject, String body);
}
