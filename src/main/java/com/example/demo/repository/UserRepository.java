package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Cacheable(value = "users", key = "#username")
    User findByUsername(String username);
}
