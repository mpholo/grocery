package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.User;

import java.util.Optional;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

public interface UserService {

    public Optional<User> findByUsername(String username);
}
