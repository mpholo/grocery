package com.mpholo.project.grocery.security.service;

/** Carries an HTTP status so the web layer can translate it to a response. */
public class AuthException extends RuntimeException {
    private final int status;

    public AuthException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
