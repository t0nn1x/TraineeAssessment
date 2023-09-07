package com.testapi.accounting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testapi.accounting.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
