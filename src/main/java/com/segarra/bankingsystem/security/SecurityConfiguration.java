package com.segarra.bankingsystem.security;

import com.segarra.bankingsystem.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled=true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.httpBasic();
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/transactions").hasAuthority("ROLE_ACCOUNTHOLDER")
                .antMatchers("/api/v1/users/accounts/**").hasAuthority("ROLE_ACCOUNTHOLDER")
                .antMatchers("/api/v1/(accounts/**|third-parties|users|checking-accounts|savings-accounts|credit-cards|student-accounts)").hasAuthority("ROLE_ADMIN")
                .antMatchers("/api/v1/third-parties/accounts/**").hasAuthority("ROLE_THIRDPARTY");

        // disabled CSRF allows POST and DELETE requests
        httpSecurity.csrf().disable();
    }
}