package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheService cacheService;

    @CacheEvict(value = "user-", key = "#id")
    public User getUserById(Long id) {
        // Fetch user from database
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @CacheEvict(value = "user-", key = "#id")
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
        cacheService.evictMultipleKeys("user-", String.valueOf(id));
        cacheService.evictMultipleKeys("users", String.valueOf(user.getUsername()));
    }

    @CacheEvict(value = "user-", key = "#id")
    public void updateUserById(Long id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setRole(user.getRole());
            existingUser.setAuthorities(user.getAuthorities());
            existingUser.setEnabled(user.isEnabled());
            userRepository.save(existingUser);
        }
    }

    @Cacheable(value = "user-", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(String username) {
        return userRepository.findByUsername(username);
    }
}
