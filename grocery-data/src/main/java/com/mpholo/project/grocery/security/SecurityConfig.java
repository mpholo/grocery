package com.mpholo.project.grocery.security;

import com.mpholo.project.grocery.security.service.UserSecurityService;
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

import static com.mpholo.project.grocery.util.MonthylGroceryMappings.MONTHLY_GROCERY_LIST;

/*******************************************************************
 Created By Mpholo Leboea-(mpholo.leboea@gmail.com) on 2020/08/02
 IDE IntelliJ IDEA
 *******************************************************************/

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Environment env;
    private final UserSecurityService userSecurityService;
    private final String SALT="salt"; //salt should be protected carefully

    public SecurityConfig(Environment env, UserSecurityService userSecurityService) {
        this.env = env;
        this.userSecurityService = userSecurityService;
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12,new SecureRandom(SALT.getBytes()));
    }

    private static final String[] PUBLIC_MATCHERS = {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/error/**/*",
            "/console/**",
            "/signup",
            "/h2-console/**",
            "/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //authorization  settinggs
        http
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHERS)
                .permitAll().anyRequest().authenticated();
        http.csrf().disable();
        http.headers().frameOptions().disable();

        //setting up login,error, and logout page
        http
                .csrf().disable().cors().disable()
                .formLogin().failureUrl("/index?error").defaultSuccessUrl(MONTHLY_GROCERY_LIST).loginPage("/index").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()
                .and()
                .rememberMe();
    }

   public void configureGlobal(AuthenticationManagerBuilder auth) throws  Exception {
       // auth.inMemoryAuthentication().withUser("user").password("user").roles("USER");
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
   }


}
