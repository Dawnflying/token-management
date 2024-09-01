package com.example.demo.service;

import com.example.demo.entity.UserSession;
import com.example.demo.repository.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    public UserSession update(String sessionId, String username) {
        UserSession userSession = userSessionRepository.findBySessionId(sessionId);
        userSession.setSessionId(sessionId);
        userSession.setUsername(username);
        return userSessionRepository.save(userSession);
    }

    public void deleteById(String sessionId) {
        userSessionRepository.deleteById(sessionId);
    }

    public UserSession findBySessionId(String sessionId) {
        return userSessionRepository.findBySessionId(sessionId);
    }

    public Iterable<UserSession> findAll() {
        return userSessionRepository.findAll();
    }

    public UserSession save(UserSession userSession) {
        return userSessionRepository.save(userSession);
    }
}
