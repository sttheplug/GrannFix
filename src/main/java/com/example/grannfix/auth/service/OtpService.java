package com.example.grannfix.auth.service;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final long TTL_SECONDS = 5 * 60;
    private final Map<String, OtpEntry> store = new ConcurrentHashMap<>();
    public String generateAndStore(String phoneNumber) {
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));
        store.put(phoneNumber, new OtpEntry(code, Instant.now().plusSeconds(TTL_SECONDS)));
        return code;
    }


    public boolean verify(String phoneNumber, String code) {
        OtpEntry entry = store.get(phoneNumber);
        if (entry == null) return false;
        if (Instant.now().isAfter(entry.expiresAt())) return false;
        if (!entry.code().equals(code)) return false;
        store.remove(phoneNumber);
        return true;
    }
    private record OtpEntry(String code, Instant expiresAt) {}
}
