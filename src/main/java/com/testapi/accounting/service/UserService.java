package com.testapi.accounting.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.testapi.accounting.entity.User;
import com.testapi.accounting.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a user by ID.
     *
     * @param id the ID of the user
     * @return an Optional<User> which contains the user if found
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a List of users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Saves a new user or updates an existing one.
     *
     * @param user the user to be saved
     * @return the saved user
     */

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the ID of the user to be deleted
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email).orElse(null);
    }
}
