package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(@Qualifier("fake") UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     return userRepository
                 .findByUsername(username)
                 .orElseThrow(() ->
                         new UsernameNotFoundException(String.format("Username %s not found.",username)));

    }



}
