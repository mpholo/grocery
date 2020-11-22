package com.mpholo.project.grocery.controller;

import com.mpholo.project.grocery.security.domain.User;
import com.mpholo.project.grocery.security.domain.UserRole;
import com.mpholo.project.grocery.security.service.RoleService;
import com.mpholo.project.grocery.security.service.UserService;
import com.mpholo.project.grocery.util.UserAttributeNames;
import com.mpholo.project.grocery.util.UserMappings;
import com.mpholo.project.grocery.util.UserViewNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Controller
@RequestMapping(UserMappings.USER_ROOT)
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(UserMappings.USER_SIGNUP)
    public String signup(Model model) {
        User user = new User();
        log.info("signup user");
        model.addAttribute(UserAttributeNames.USER,new User());
        return UserViewNames.USER_SINGUP;
    }

    @PostMapping(UserMappings.USER_SIGNUP)
    public  String singupPost(@ModelAttribute("user") User user,Model model) {

        if(userService.checkUserExists(user.getUsername(),user.getEmail())) {

            if(userService.checkEmailExists(user.getUsername())) {
                model.addAttribute("emailExists",true);
            }
            if(userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists",true);
            }


          return UserMappings.USER_SIGNUP;
        } else  {
            Set<UserRole> userRole= new HashSet<>();
            String role ="ROLE_USER";
            userRole.add(new UserRole(user,roleService.findByName(role).
                    orElseThrow(()-> new IllegalArgumentException("Role "+role+" does not exist"))));
            userService.createUser(user,userRole);
            return "redirect:/login";
        }

    }


}
