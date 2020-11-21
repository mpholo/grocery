package com.mpholo.project.grocery.security;

//import com.mpholo.project.grocery.security.service.UserSecurityService;

import com.mpholo.project.grocery.security.service.UserSecurityService;
import com.mpholo.project.grocery.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;

import static com.mpholo.project.grocery.util.MonthylGroceryMappings.MONTHLY_GROCERY_REDIRECT_LIST;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private  Environment env;
    private final UserSecurityService userSecurityService;

    private static final String SALT="salt"; //salt should be protected careefully

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12,new SecureRandom(SALT.getBytes()));
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/error/**/*",
            "/console/**",
            "/h2-console/**",
            "/",
            "/security/**",
            "/grocery-items/**",
            "/monthly-grocery/**"
    };

    public SecurityConfig(UserService userService,
                          Environment env, UserSecurityService userSecurityService1) {
        this.userService = userService;
        this.env = env;
        this.userSecurityService =userSecurityService1;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //authorization  settings
         http
                .authorizeRequests().
                antMatchers(PUBLIC_MATCHERS).
                permitAll().anyRequest().authenticated();

           http
                .csrf().disable().cors().disable()
                .formLogin().failureUrl("/index?error").defaultSuccessUrl(MONTHLY_GROCERY_REDIRECT_LIST).loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe();

           //allow connection to h2-console
           http.headers()
                   .frameOptions()
                   .sameOrigin();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }

}
