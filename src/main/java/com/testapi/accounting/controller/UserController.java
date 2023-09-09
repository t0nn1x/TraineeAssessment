package com.testapi.accounting.controller;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testapi.accounting.constant.UserRole;
import com.testapi.accounting.entity.User;
import com.testapi.accounting.service.FileStorageService;
import com.testapi.accounting.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private FileStorageService fileStorageService;

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<?> createUser( // ResponseEntity<?> is used to return any type of response
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "role", required = false, defaultValue = "NORMAL") UserRole role,
            @RequestParam(value = "loggedInAt", required = false) LocalDateTime loggedInAt,
            @RequestParam(value = "updatedAt", required = false) LocalDateTime updatedAt) throws IOException {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role); // This will set the role to NORMAL if not provided in the request

        if (email == null || email.isEmpty() || email.length() > 255 || !email.contains("@")) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        if (password == null || password.length() < 8) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters.");
        }

        if (loggedInAt != null) {
            user.setLoggedInAt(loggedInAt);
        }
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt);
        }

        if (file != null && !file.isEmpty()) {
            String fileName = fileStorageService.storeFile(file);
            user.setPhotoPath(fileName);
        }

        return ResponseEntity.ok(userService.save(user));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "role", required = false) UserRole role,
            @RequestParam(value = "loggedInAt", required = false) LocalDateTime loggedInAt,
            @RequestParam(value = "updatedAt", required = false) LocalDateTime updatedAt,
            Principal principal) throws IOException {
        return userService.findById(id).map(user -> {
            if (!principal.getName().equals("ADMIN") && !principal.getName().equals(email)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
            }

            if (email != null) {
                user.setEmail(email);
            }
            if (password != null) {
                user.setPassword(password);
            }
            if (role != null) {
                user.setRole(role);
            }
            if (loggedInAt != null) {
                user.setLoggedInAt(loggedInAt);
            }
            if (updatedAt != null) {
                user.setUpdatedAt(updatedAt);
            }

            if (file != null && !file.isEmpty()) {
                String fileName = fileStorageService.storeFile(file);
                user.setPhotoPath(fileName);
            }

            if (email != null && (email.isEmpty() || email.length() > 255 || !email.contains("@"))) {
                return ResponseEntity.badRequest().body("Invalid email format.");
            }

            if (password != null && password.length() < 8) {
                return ResponseEntity.badRequest().body("Password must be at least 8 characters.");
            }

            return ResponseEntity.ok(userService.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, Principal principal) {
        User userToDelete = userService.findById(id).orElse(null);
        if (userToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        if (!principal.getName().equals("ADMIN") && !principal.getName().equals(userToDelete.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
