package com.mpholo.project.grocery.security.repositories;

import com.google.common.collect.Lists;
import com.mpholo.project.grocery.security.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class UserRepositoryFake  implements UserRepository {

    private final PasswordEncoder passwordEncoder;

    public UserRepositoryFake(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return getUsers()
                .stream()
                .filter(applicationUser->username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return getUsers()
                .stream()
                .filter(applicationUser->email.equals(applicationUser.getEmail()))
                .findFirst();
    }

    private List<User> getUsers() {
        User user1 =new User();
        user1.setUsername("user1");
        user1.setEmail("user1@email.com");
        user1.setPassword(passwordEncoder.encode("pass1"));

        User user2 =new User();
        user2.setUsername("user2");
        user2.setEmail("user2@email.com");
        user2.setPassword(passwordEncoder.encode("pass2"));

        User user3 =new User();
        user3.setUsername("user3");
        user3.setEmail("user3@email.com");
        user3.setPassword(passwordEncoder.encode("pass3"));


        List<User> users = Lists.newArrayList(user1,user2,user3);

        return  users;
    }

}
