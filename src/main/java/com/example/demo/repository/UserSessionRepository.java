package com.example.demo.repository;

import com.example.demo.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    /**
     * 根据sessionId查找用户会话
     *
     * @param sessionId 会话ID
     * @return 用户会话
     */
    UserSession findBySessionId(String sessionId);
}
