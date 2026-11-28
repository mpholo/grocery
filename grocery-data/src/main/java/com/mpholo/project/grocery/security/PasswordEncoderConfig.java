package com.mpholo.project.grocery.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;

/**
 * The password encoder lives in its own dependency-free config so it can't form
 * a cycle with SecurityConfig (which depends on services that use the encoder).
 * BCrypt hashes embed their salt, so existing hashes verify regardless of the
 * SecureRandom seed — this keeps the original (strength 12) encoder unchanged.
 */
@Configuration
public class PasswordEncoderConfig {

    private static final String SALT = "salt";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }
}
