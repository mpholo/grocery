package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.controller.api.v1.dto.AuthDtos.PendingUserResponse;
import com.mpholo.project.grocery.security.service.AuthException;
import com.mpholo.project.grocery.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Superadmin-only account administration. Access is enforced by the API
 * security chain ({@code /api/admin/** -> hasRole('SUPERADMIN')}).
 */
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    private final AuthService authService;

    public AdminRestController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/pending")
    public List<PendingUserResponse> pending() {
        return authService.listPending().stream()
                .map(u -> new PendingUserResponse(
                        u.getUserId(), u.getFirstName(), u.getLastName(),
                        u.getEmail(), u.getCreatedDate()))
                .collect(Collectors.toList());
    }

    @PostMapping("/users/{userId}/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@PathVariable Long userId) {
        authService.approve(userId);
    }

    @PostMapping("/users/{userId}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable Long userId) {
        authService.reject(userId);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuth(AuthException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
