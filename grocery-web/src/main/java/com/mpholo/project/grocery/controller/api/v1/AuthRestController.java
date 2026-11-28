package com.mpholo.project.grocery.controller.api.v1;

import com.mpholo.project.grocery.controller.api.v1.dto.AuthDtos.ActivateRequest;
import com.mpholo.project.grocery.controller.api.v1.dto.AuthDtos.LoginRequest;
import com.mpholo.project.grocery.controller.api.v1.dto.AuthDtos.LoginResponse;
import com.mpholo.project.grocery.controller.api.v1.dto.AuthDtos.ResetPasswordRequest;
import com.mpholo.project.grocery.controller.api.v1.dto.AuthDtos.SignupRequest;
import com.mpholo.project.grocery.security.service.AuthException;
import com.mpholo.project.grocery.security.service.AuthService;
import com.mpholo.project.grocery.security.service.AuthService.LoginResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * Public authentication API for the app: self-service signup (then superadmin
 * approval + email activation), JWT login, and password reset.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    private final AuthService authService;

    public AuthRestController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody SignupRequest req) {
        authService.signup(req.getFirstName(), req.getLastName(), req.getEmail(), req.getPhone());
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest req) {
        LoginResult r = authService.login(req.getUsername(), req.getPassword());
        return new LoginResponse(r.token, r.userId, r.username, r.role,
                r.firstName, r.lastName, r.expiresInMs);
    }

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(@RequestBody ActivateRequest req) {
        authService.activate(req.getToken(), req.getUsername(), req.getPassword());
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@RequestParam("identifier") String identifier) {
        authService.forgotPassword(identifier);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetPassword(@RequestBody ResetPasswordRequest req) {
        authService.resetPassword(req.getToken(), req.getPassword());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuth(AuthException e) {
        return ResponseEntity.status(e.getStatus())
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
