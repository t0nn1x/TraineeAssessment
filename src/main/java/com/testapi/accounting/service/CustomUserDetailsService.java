package com.testapi.accounting.service;


import com.testapi.accounting.entity.User;
import com.testapi.accounting.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = optionalUser.get();

        // Set the loggedInAt field to the current time
        user.setLoggedInAt(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }
}
