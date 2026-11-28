package com.mpholo.project.grocery.security;

//import com.mpholo.project.grocery.security.service.UserSecurityService;

import com.mpholo.project.grocery.security.service.UserSecurityService;
import com.mpholo.project.grocery.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.mpholo.project.grocery.util.MonthylGroceryMappings.MONTHLY_GROCERY_REDIRECT_LIST;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private  Environment env;
    private final UserSecurityService userSecurityService;
    private final BCryptPasswordEncoder passwordEncoder;

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
            "/activate",
            "/activate/**",
            "/reset-password",
            "/reset-password/**",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**"
    };

    public SecurityConfig(UserService userService,
                          Environment env, UserSecurityService userSecurityService1,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.env = env;
        this.userSecurityService =userSecurityService1;
        this.passwordEncoder = passwordEncoder;
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
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
    }

}
