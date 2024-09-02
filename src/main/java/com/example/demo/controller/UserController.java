package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/get")
    public String get(String username) {
        return userService.getUserByUsername(username).toString();
    }

    @DeleteMapping("/delete")
    public void delete(Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping("/update")
    public void update(String username, String password) {
        User user = userService.getUserByUsername(username);
        user.setUsername(username);
        user.setPassword(password);
        userService.updateUserById(user.getId(), user);
    }
}
