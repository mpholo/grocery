package com.mpholo.project.grocery.security.repositories;

import com.mpholo.project.grocery.security.domain.User;

import java.util.Optional;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

public interface UserRepository  {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
