package com.example.demo.controller;

import com.example.demo.entity.UserSession;
import com.example.demo.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userSession")
public class UserSessionController {
    @Autowired
    private UserSessionService userSessionService;


    @PutMapping("/update")
    public UserSession update(String sessionId, String username) {
        UserSession userSession = userSessionService.findBySessionId(sessionId);
        userSession.setSessionId(sessionId);
        userSession.setUsername(username);
        return userSessionService.save(userSession);
    }

    @PutMapping("/delete")
    public void delete(String sessionId) {
        userSessionService.deleteById(sessionId);
    }

    @GetMapping("/get")
    public UserSession get(@RequestParam("session_id") String sessionId) {
        return userSessionService.findBySessionId(sessionId);
    }

    @GetMapping("/getAll")
    public Iterable<UserSession> getAll() {
        return userSessionService.findAll();
    }

    @GetMapping("/findByQueryParam")
    public Iterable<UserSession> findByQueryParam(@RequestParam("page") int page, @RequestParam("size") int size) {
        PageRequest request = PageRequest.of(page, size);
        return userSessionService.findByQueryParam(request);
    }
}
