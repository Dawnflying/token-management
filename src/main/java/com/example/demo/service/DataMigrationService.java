package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserSession;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.session.StandardSession;
import org.slf4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataMigrationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserSessionRepository userSessionRepository;

    public void migrateData(Integer pageSize) {
        int currentPage = 0;
        Page<UserSession> page;

        do {
            page = getSessions(currentPage, pageSize);
            try {
                List<UserSession> userSessions = page.getContent();
                userSessionRepository.saveAll(userSessions);
            } catch (Exception e) {
                log.error("Failed to get sessions from page " + currentPage, e);
                e.printStackTrace();
            }
            currentPage++;
        } while (page.hasNext());
    }

    @CacheEvict(value = "users", allEntries = true)
    public void validateDate(Integer pageSize) {
        int currentPage = 0;
        Page<UserSession> page;

        do {
            page = getSessions(currentPage, pageSize);
            try {
                List<UserSession> userSessions = page.getContent();
                userSessionRepository.saveAll(userSessions);
            } catch (Exception e) {
                log.error("Failed to get sessions from page " + currentPage, e);
                e.printStackTrace();
            }
            currentPage++;
        } while (page.hasNext());
    }

    public Page<UserSession> getSessions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query();
        query.with(pageable);
        String collectionName = "sessions";

        long count = mongoTemplate.count(query, HashMap.class,collectionName);
        List<HashMap> sessions = mongoTemplate.find(query, HashMap.class, collectionName);
        List<UserSession> userSessions = sessions.stream().map(x -> {
            UserSession session = new UserSession();
            session.setUsername((String) x.get("principal"));
            session.setSessionId((String) x.get("_id"));
            session.setCreateTime(((Date) x.get("created")).getTime());
            session.setExpireTime(((Date) x.get("expireAt")).getTime());
            HttpSession httpSession = new StandardSession(null);
            httpSession.getAttribute("username");
            return session;
        }).collect(Collectors.toList());
        return new PageImpl<>(userSessions, pageable, count);
    }


    public long getSessionExpiryTime(HttpSession session) {
        // 获取会话的创建时间
        long creationTime = session.getCreationTime();

        // 获取会话的超时时间（单位：分钟）
        int maxInactiveInterval = session.getMaxInactiveInterval() / 60;

        // 计算过期时间
        long currentTime = System.currentTimeMillis();
        long expirationTime = creationTime + (maxInactiveInterval * 60 * 1000);

        return expirationTime;
    }
}

