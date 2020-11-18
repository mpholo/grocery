package com.mpholo.project.grocery.security.repositories;

import com.mpholo.project.grocery.security.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("JPA")
public interface UserRepositoryJpa extends JpaRepository<User,Long>,UserRepository   {

}
