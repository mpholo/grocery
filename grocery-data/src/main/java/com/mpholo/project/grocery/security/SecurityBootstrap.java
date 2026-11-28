package com.mpholo.project.grocery.security;

import com.mpholo.project.grocery.security.domain.AccountStatus;
import com.mpholo.project.grocery.security.domain.Role;
import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.domain.UserRole;
import com.mpholo.project.grocery.security.repositories.RoleRepository;
import com.mpholo.project.grocery.security.repositories.UserRepository;
import com.mpholo.project.grocery.security.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Ensures the ROLE_USER / ROLE_SUPERADMIN roles exist and, if SUPERADMIN_*
 * env vars are supplied, creates an active superadmin on first run.
 */
@Component
@Slf4j
public class SecurityBootstrap implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final String superUsername;
    private final String superPassword;
    private final String superEmail;

    public SecurityBootstrap(RoleRepository roleRepository, UserRepository userRepository,
                             BCryptPasswordEncoder passwordEncoder,
                             @Value("${SUPERADMIN_USERNAME:}") String superUsername,
                             @Value("${SUPERADMIN_PASSWORD:}") String superPassword,
                             @Value("${SUPERADMIN_EMAIL:}") String superEmail) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.superUsername = superUsername;
        this.superPassword = superPassword;
        this.superEmail = superEmail;
    }

    @Override
    public void run(String... args) {
        Role userRole = ensureRole(AuthService.ROLE_USER);
        Role superRole = ensureRole(AuthService.ROLE_SUPERADMIN);

        if (superUsername.isBlank() || superPassword.isBlank() || superEmail.isBlank()) {
            log.info("SUPERADMIN_* not fully set — skipping superadmin bootstrap");
            return;
        }
        if (userRepository.findByUsername(superUsername).isPresent()
                || userRepository.existsByEmail(superEmail)) {
            log.info("Superadmin already present — skipping bootstrap");
            return;
        }
        User admin = new User();
        admin.setUsername(superUsername);
        admin.setEmail(superEmail);
        admin.setPassword(passwordEncoder.encode(superPassword));
        admin.setFirstName("Super");
        admin.setLastName("Admin");
        admin.setEnabled(true);
        admin.setStatus(AccountStatus.ACTIVE);
        admin.setCreatedDate(LocalDateTime.now());
        Set<UserRole> roles = new HashSet<>();
        roles.add(new UserRole(admin, superRole));
        admin.setUserRoles(roles);
        userRepository.save(admin);
        log.info("Bootstrapped superadmin '{}'", superUsername);
    }

    private Role ensureRole(String name) {
        return roleRepository.findByName(name).orElseGet(() -> {
            Role r = new Role();
            r.setName(name);
            Role saved = roleRepository.save(r);
            log.info("Created role {}", name);
            return saved;
        });
    }
}
