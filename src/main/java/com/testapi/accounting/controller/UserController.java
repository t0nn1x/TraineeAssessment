package com.testapi.accounting.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.testapi.accounting.entity.User;
import com.testapi.accounting.service.UserService;

/**
 * Controller for user operations.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    /**
     * Constructor for UserController.
     * 
     * @param userService UserService object.
     */
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     * 
     * @return ResponseEntity containing a list of User objects.
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Get a specific user by ID.
     * 
     * @param id ID of the user.
     * @return ResponseEntity containing the User object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new user.
     * 
     * @param user User object to be created.
     * @param file MultipartFile for the user's photo.
     * @return ResponseEntity containing the created User object or error message.
     * @throws IOException if there's an error during file storage.
     */
    @PostMapping
    public ResponseEntity<?> createUser(User user, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        return ResponseEntity.ok(userService.createUser(user, file));
    }

    /**
     * Update an existing user.
     * 
     * @param id          ID of the user to be updated.
     * @param updatedUser Updated User object.
     * @param file        MultipartFile for the updated user's photo.
     * @return ResponseEntity containing the updated User object or error message.
     * @throws IOException if there's an error during file storage.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, User updatedUser,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        try {
            return ResponseEntity.ok(userService.updateUser(id, updatedUser, file));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Delete a specific user by ID.
     * 
     * @param id ID of the user to be deleted.
     * @return ResponseEntity indicating the result of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
