package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

@Configuration
@EnableMongoHttpSession // 启用 MongoDB 存储的 HttpSession
public class SessionConfig {
    // 这里可以添加更多配置
}
