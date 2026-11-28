package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.repositories.UserRepository;
import com.mpholo.project.grocery.service.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Resolves the authenticated user for per-user data scoping. The principal is a
 * {@link User} for both the JWT API filter and the web form login.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserDetails)) {
            // No authenticated user — treat scoped data as absent.
            throw new ResourceNotFoundException();
        }
        String username = ((UserDetails) auth.getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(ResourceNotFoundException::new);
    }

    /** True if [owner] is the current user. Used to authorize per-row access. */
    public boolean ownedByCurrent(User owner) {
        if (owner == null) {
            return false;
        }
        return owner.getUserId().equals(requireCurrentUser().getUserId());
    }
}
