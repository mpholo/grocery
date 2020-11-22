package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByName(String name);
}
