package com.mpholo.project.grocery.controller.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/** Request/response bodies for the /api/auth and /api/admin endpoints. */
public final class AuthDtos {

    private AuthDtos() {}

    @Data
    public static class SignupRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginResponse {
        private String token;
        private Long userId;
        private String username;
        private String role;
        private String firstName;
        private String lastName;
        private long expiresInMs;
    }

    @Data
    public static class ActivateRequest {
        private String token;
        private String username;
        private String password;
    }

    @Data
    public static class ResetPasswordRequest {
        private String token;
        private String password;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PendingUserResponse {
        private Long userId;
        private String firstName;
        private String lastName;
        private String email;
        private LocalDateTime createdDate;
    }
}
