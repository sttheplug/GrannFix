package com.example.grannfix.common.errors;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}