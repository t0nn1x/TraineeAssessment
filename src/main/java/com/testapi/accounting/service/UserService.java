package com.testapi.accounting.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.testapi.accounting.entity.User;
import com.testapi.accounting.repository.UserRepository;
import com.testapi.accounting.constant.UserRole;

/**
 * Service for user operations.
 */

@Service
public class UserService {
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService; 

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor for UserService.
     * 
     * @param userRepository UserRepository object.
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.fileStorageService = new FileStorageService();
    }

    /**
     * Find a user by ID.
     * 
     * @param id ID of the user.
     * @return Optional containing the User object.
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Find all users.
     * 
     * @return List of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Save a user.
     * 
     * @param user User object to be saved.
     * @return Saved User object.
     */
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     * 
     * @param id ID of the user to be deleted.
     */
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Find a user by email.
     * 
     * @param email Email of the user.
     * @return User object.
     */
    public User findByEmail(String email) {
        return (User) userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Create a new user.
     * 
     * @param user User object to be created.
     * @param file MultipartFile for the user's photo.
     * @return Created User object.
     * @throws IOException if there's an error during file storage.
     */
    public User createUser(User user, MultipartFile file) throws IOException {
        if (user.getEmail() == null || user.getEmail().isEmpty() || user.getEmail().length() > 255 || !user.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid email format.");
        }

        if (user.getPassword() == null || user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            user.setPhotoPath(fileName);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Update an existing user.
     * 
     * @param id          ID of the user to be updated.
     * @param updatedUser Updated User object.
     * @param file        MultipartFile for the updated user's photo.
     * @return Updated User object.
     * @throws IOException if there's an error during file storage.
     */
    public User updateUser(Long id, User updatedUser, MultipartFile file) throws IOException {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (updatedUser.getEmail() != null) {
            if (updatedUser.getEmail().isEmpty() || updatedUser.getEmail().length() > 255
                    || !updatedUser.getEmail().contains("@")) {
                throw new IllegalArgumentException("Invalid email format.");
            }
            user.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getPassword() != null && updatedUser.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            user.setPhotoPath(fileName);
        }

        if (updatedUser.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        if (updatedUser.getRole() != null) {
            user.setRole(updatedUser.getRole());
        }

        return userRepository.save(user);
    }
}