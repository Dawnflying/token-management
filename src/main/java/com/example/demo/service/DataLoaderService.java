package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
public class DataLoaderService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void loadData() throws Exception {
        List<String> sqlStatements = Files.readAllLines(Paths.get("insert_statements.sql"));
        for (int i = 1; i <= 50; i++) {
            String username = "user" + i;
            String email = "user" + i + "@example.com";
            String password = "password" + i;
            String role = "ROLE_USER";
            String authorities = "USER";
            boolean enabled = true;

            User user = new User();

            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setAuthorities(authorities);
            user.setEnabled(enabled);

            userRepository.save(user);
        }
    }
}
