package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.security.service.AuthException;
import com.mpholo.project.grocery.security.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Public web pages reached from the activation / password-reset emails. The
 * links are {@code /activate?token=...} and {@code /reset-password?token=...}.
 */
@Controller
public class AccountActivationController {

    private final AuthService authService;

    public AccountActivationController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/activate")
    public String activateForm(@RequestParam(value = "token", required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "activate";
    }

    @PostMapping("/activate")
    public String activateSubmit(@RequestParam String token,
                                 @RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {
        model.addAttribute("token", token);
        model.addAttribute("username", username);
        try {
            authService.activate(token, username, password);
            model.addAttribute("success", true);
        } catch (AuthException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "activate";
    }

    @GetMapping("/reset-password")
    public String resetForm(@RequestParam(value = "token", required = false) String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetSubmit(@RequestParam String token,
                              @RequestParam String password,
                              Model model) {
        model.addAttribute("token", token);
        try {
            authService.resetPassword(token, password);
            model.addAttribute("success", true);
        } catch (AuthException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "reset-password";
    }
}
