package com.mpholo.project.grocery.security.service;

import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public void saveUser(User user) {

    }

//    @Transactional
//    public void createUser(User user, Set<UserRole> userRoles) {
//        Optional<User> localUser = userRepository.findByUsername(user.getUsername());
//        localUser.ifPresent(u ->{
//            String encryptedPassword = passwordEncoder.encode(u.getPassword());
//            user.setPassword(encryptedPassword);
//
//            for(UserRole ur:userRoles) {
//
//            }
//        });
//
//       userRepository.save(user);
//    }




    @Override
    public List<User> findUserList() {
        return userRepository.findAll();
    }




}
