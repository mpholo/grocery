package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.domain.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean checkUserExists(String username,String email);


    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);

    void save(User user);

    List<User> findUserList();

    User createUser(User user, Set<UserRole> userRoles);




}
