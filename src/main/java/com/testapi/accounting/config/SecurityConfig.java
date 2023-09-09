package com.testapi.accounting.config;

import com.testapi.accounting.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Disable CSRF for simplicity
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()  // Allow all requests to /public/** without authentication
                .antMatchers("/admin/**").hasAuthority("ADMIN") // Only ADMIN can access /admin/** paths
                .antMatchers(HttpMethod.POST, "/users").permitAll()  // Allow POST requests to /users without authentication
                .anyRequest().authenticated()  // Any other request needs to be authenticated
            .and()
            .httpBasic();  // Use basic authentication for simplicity
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }
}
