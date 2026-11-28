package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.JwtService;
import com.mpholo.project.grocery.security.domain.AccountStatus;
import com.mpholo.project.grocery.security.domain.Role;
import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.domain.UserRole;
import com.mpholo.project.grocery.security.repositories.RoleRepository;
import com.mpholo.project.grocery.security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Self-service signup with superadmin approval + email activation, mirroring the
 * rental app's approval flow: signup -> PENDING -> superadmin approve -> setup
 * link emailed -> user sets username/password (ACTIVE) -> JWT login.
 */
@Service
@Slf4j
public class AuthService {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_SUPERADMIN = "ROLE_SUPERADMIN";

    private static final long ACTIVATION_TTL_HOURS = 24 * 7;
    private static final long RESET_TTL_HOURS = 1;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AccountMailService mailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserSecurityService userSecurityService;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository,
                       BCryptPasswordEncoder passwordEncoder, AccountMailService mailService,
                       JwtService jwtService, AuthenticationManager authenticationManager,
                       UserSecurityService userSecurityService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userSecurityService = userSecurityService;
    }

    // ---- Signup (no credentials yet; those are set at activation) ----
    @Transactional
    public void signup(String firstName, String lastName, String email, String phone) {
        if (email == null || email.isBlank()) {
            throw new AuthException(400, "Email is required");
        }
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(409, "Email already registered");
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUsername("pending-" + UUID.randomUUID().toString().substring(0, 12));
        user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // throwaway
        user.setEnabled(false);
        user.setStatus(AccountStatus.PENDING);
        user.setCreatedDate(LocalDateTime.now());
        user.setUserRoles(rolesFor(user, ROLE_USER));
        userRepository.save(user);

        mailService.signupReceived(user);
        mailService.notifyPendingApproval(superadminEmails(), user);
        log.info("Signup received for {} (pending approval)", email);
    }

    public List<User> listPending() {
        return userRepository.findByStatus(AccountStatus.PENDING);
    }

    // ---- Superadmin approval ----
    @Transactional
    public void approve(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(404, "User not found"));
        if (user.getStatus() != AccountStatus.PENDING) {
            throw new AuthException(400, "User is not pending approval");
        }
        user.setStatus(AccountStatus.APPROVED);
        user.setActivationToken(UUID.randomUUID().toString());
        user.setActivationTokenExpires(LocalDateTime.now().plusHours(ACTIVATION_TTL_HOURS));
        userRepository.save(user);
        mailService.accountSetupLink(user, user.getActivationToken());
        log.info("Approved {} — setup link emailed", user.getEmail());
    }

    @Transactional
    public void reject(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(404, "User not found"));
        user.setStatus(AccountStatus.REJECTED);
        user.setEnabled(false);
        userRepository.save(user);
        log.info("Rejected signup {}", user.getEmail());
    }

    // ---- Activation: user sets username + password via the emailed link ----
    @Transactional
    public void activate(String token, String username, String rawPassword) {
        if (username == null || username.isBlank()) {
            throw new AuthException(400, "Username is required");
        }
        validatePassword(rawPassword);
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new AuthException(400, "Invalid or used activation link"));
        if (user.getActivationTokenExpires() == null
                || user.getActivationTokenExpires().isBefore(LocalDateTime.now())) {
            throw new AuthException(400, "Activation link has expired");
        }
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent() && !existing.get().getUserId().equals(user.getUserId())) {
            throw new AuthException(409, "Username is taken");
        }
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setStatus(AccountStatus.ACTIVE);
        user.setEnabled(true);
        user.setActivationToken(null);
        user.setActivationTokenExpires(null);
        userRepository.save(user);
        log.info("Account activated: {}", username);
    }

    // ---- Login -> JWT ----
    public LoginResult login(String username, String rawPassword) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, rawPassword));
        } catch (AuthenticationException e) {
            throw new AuthException(401, "Invalid credentials or account not active");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException(401, "Invalid credentials or account not active"));
        UserDetails details = userSecurityService.loadUserByUsername(username);
        String role = user.getAuthorities().stream().findFirst()
                .map(a -> a.getAuthority()).orElse(ROLE_USER);
        String token = jwtService.generateToken(details, role);
        return new LoginResult(token, user.getUserId(), user.getUsername(), role,
                user.getFirstName(), user.getLastName(), jwtService.getExpirationMs());
    }

    // ---- Forgot / reset password (reuses the activation token columns) ----
    @Transactional
    public void forgotPassword(String identifier) {
        Optional<User> user = userRepository.findByUsername(identifier);
        if (user.isEmpty()) {
            user = userRepository.findByEmail(identifier);
        }
        // Always behave the same to avoid leaking which accounts exist.
        user.ifPresent(u -> {
            u.setActivationToken(UUID.randomUUID().toString());
            u.setActivationTokenExpires(LocalDateTime.now().plusHours(RESET_TTL_HOURS));
            userRepository.save(u);
            mailService.passwordResetLink(u, u.getActivationToken());
        });
    }

    @Transactional
    public void resetPassword(String token, String rawPassword) {
        validatePassword(rawPassword);
        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new AuthException(400, "Invalid or used reset link"));
        if (user.getActivationTokenExpires() == null
                || user.getActivationTokenExpires().isBefore(LocalDateTime.now())) {
            throw new AuthException(400, "Reset link has expired");
        }
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setActivationToken(null);
        user.setActivationTokenExpires(null);
        userRepository.save(user);
        log.info("Password reset for {}", user.getUsername());
    }

    // ---- helpers ----
    private void validatePassword(String password) {
        if (password == null || password.length() < 6
                || password.chars().noneMatch(Character::isUpperCase)
                || password.chars().noneMatch(Character::isDigit)) {
            throw new AuthException(400,
                    "Password must be at least 6 characters with an uppercase letter and a number");
        }
    }

    private Set<UserRole> rolesFor(User user, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AuthException(500, "Role " + roleName + " not configured"));
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(user, role));
        return roles;
    }

    private List<String> superadminEmails() {
        return userRepository.findAll().stream()
                .filter(u -> u.getAuthorities().stream()
                        .anyMatch(a -> ROLE_SUPERADMIN.equals(a.getAuthority())))
                .map(User::getEmail)
                .filter(e -> e != null && !e.isEmpty())
                .collect(Collectors.toList());
    }

    /** Lightweight login result; the web layer maps this to its JSON DTO. */
    public static class LoginResult {
        public final String token;
        public final Long userId;
        public final String username;
        public final String role;
        public final String firstName;
        public final String lastName;
        public final long expiresInMs;

        public LoginResult(String token, Long userId, String username, String role,
                           String firstName, String lastName, long expiresInMs) {
            this.token = token;
            this.userId = userId;
            this.username = username;
            this.role = role;
            this.firstName = firstName;
            this.lastName = lastName;
            this.expiresInMs = expiresInMs;
        }
    }
}
