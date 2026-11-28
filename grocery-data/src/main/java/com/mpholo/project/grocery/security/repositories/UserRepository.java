package com.mpholo.project.grocery.security.repositories;

import com.mpholo.project.grocery.security.domain.AccountStatus;
import com.mpholo.project.grocery.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long>   {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByActivationToken(String activationToken);
    List<User> findByStatus(AccountStatus status);
}
