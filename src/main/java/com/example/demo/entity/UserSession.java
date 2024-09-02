package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "user_session",
        indexes = {
                @Index(name = "idx_username", columnList = "username"),
                @Index(name = "idx_session_id", columnList = "session_id")
        }, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"session_id"})
})
public class UserSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String role;

    @Column(name = "session_id")
    private String sessionId;

    private long expireTime;

    private long createTime;
}
