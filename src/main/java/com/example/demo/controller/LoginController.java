package com.example.demo.controller;

import com.example.demo.component.JwtManager;
import com.example.demo.domain.DemoResponse;
import com.example.demo.domain.LoginRequest;
import com.example.demo.entity.User;
import com.example.demo.service.DataLoaderService;
import com.example.demo.service.DataMigrationService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/public")
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtManager jwtManager;

    @Resource
    private DataLoaderService dataLoaderService;

    @Resource
    private DataMigrationService dataMigrationService;

    @Resource
    private UserService userService;
    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public DemoResponse<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        // 用户身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getUserByUsername(loginRequest.getUsername());
        session.setAttribute("username", loginRequest.getUsername());
        session.setAttribute("authorities", authentication.getAuthorities());
        session.setAttribute("userId", user.getId());
        return new DemoResponse<String>().success(session.getId());
    }

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/newLogin")
    public DemoResponse<String> newLogin(@RequestBody LoginRequest loginRequest) {
        // 用户身份验证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.getUserByUsername(loginRequest.getUsername());
        String token = jwtManager.generateToken(loginRequest.getUsername(), user.getId(), authentication.getAuthorities().toString());
        return new DemoResponse<String>().success(token);
    }

    /**
     * 用户登出
     *
     * @return 登出结果
     */
    @PostMapping("/logout")
    public DemoResponse<String> logout(HttpSession httpSession) {
        httpSession.invalidate();
        return new DemoResponse<String>().success("登出成功");
    }

    /**
     * 加载数据
     *
     * @return 加载结果
     */
    @GetMapping("/loadData")
    public DemoResponse<String> hello() {
        try {
            dataLoaderService.loadData();
            return new DemoResponse<String>().success("Data Loaded");
        } catch (Exception e) {
            return new DemoResponse<String>().success("Error loading data: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public DemoResponse<String> validate() {
        dataMigrationService.validateData(20);
        return new DemoResponse<String>().success("validate success");
    }

    @PostMapping("/migrate")
    public DemoResponse<String> migrate() {
        dataMigrationService.migrateData(20);
        return new DemoResponse<String>().success("migrate success");
    }
}
