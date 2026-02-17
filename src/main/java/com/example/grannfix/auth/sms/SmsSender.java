package com.example.grannfix.auth.sms;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsSender {

    @Value("${twilio.from-number}")
    private String fromNumber;

    public void send(String toNumber, String text) {
        try {
            Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromNumber),
                    text
            ).create();
        } catch (Exception e) {
            throw new RuntimeException("Failed to send SMS", e);
        }
    }
}
