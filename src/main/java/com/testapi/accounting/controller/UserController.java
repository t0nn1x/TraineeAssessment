package com.testapi.accounting.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    public UserController(UserService userService){
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.findAll());
    }

    // Get user by id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Create a new user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestParam("file") MultipartFile file, @RequestParam("user") String userJson) throws IOException{
        User user = new ObjectMapper().readValue(userJson, User.class);
        String fileName = fileStorageService.storeFile(file);
        user.setPhotoPath(fileName);
        return ResponseEntity.ok(userService.save(user));
    }


    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam("user") String userJson) throws IOException{
        User updatedUser = new ObjectMapper().readValue(userJson, User.class);
        String fileName = fileStorageService.storeFile(file);
        updatedUser.setPhotoPath(fileName);
        
        return userService.findById(id).map(user -> {
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setRole(updatedUser.getRole());
            user.setLoggedInAt(updatedUser.getLoggedInAt());
            user.setUpdatedAt(updatedUser.getUpdatedAt());
            user.setPhotoPath(updatedUser.getPhotoPath());
            
            return ResponseEntity.ok(userService.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
