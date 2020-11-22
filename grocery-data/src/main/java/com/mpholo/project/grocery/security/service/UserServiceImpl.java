package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.domain.UserRole;
import com.mpholo.project.grocery.security.repositories.RoleRepository;
import com.mpholo.project.grocery.security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private  BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean checkUserExists(String username, String email) {
        if(checkUsernameExists(username) || checkEmailExists(username))
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUsernameExists(String username) {
        if(findByUsername(username).isPresent()) {
            return  true;
        }
        return false;
    }

    @Override
    public boolean checkEmailExists(String email) {
        if(findByEmail(email).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void save(User user) {

    }

    @Transactional
    public User createUser(User user, Set<UserRole> userRoles) {
        Optional<User> localUser = userRepository.findByUsername(user.getUsername());
     if(!localUser.isPresent()) {
         String encryptedPassword = passwordEncoder.encode(user.getPassword());
         user.setPassword(encryptedPassword);

         for(UserRole ur:userRoles) {
             roleRepository.save(ur.getRole());
         }
         localUser = Optional.ofNullable(userRepository.save(user));
     } else {
         log.info("User with username {} alreadt exist. Nothing will be done");
     }

     return localUser.get();
    }




    @Override
    public List<User> findUserList() {
        return userRepository.findAll();
    }




}
