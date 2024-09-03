package com.example.demo.repository;

import com.example.demo.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    /**
     * 根据sessionId查找用户会话
     *
     * @param sessionId 会话ID
     * @return 用户会话
     */
    UserSession findBySessionId(String sessionId);


    // 根据 sessionId 列表查询
    @Query("SELECT u FROM UserSession u WHERE u.sessionId IN :sessionIds")
    List<UserSession> findBySessionIdIn(@Param("sessionIds") List<String> sessionIds);
}
