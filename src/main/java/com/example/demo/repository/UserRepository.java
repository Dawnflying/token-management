package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    @Cacheable(value = "users", key = "#username")
    User findByUsername(String username);
}
