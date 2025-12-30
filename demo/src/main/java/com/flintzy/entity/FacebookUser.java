package com.flintzy.entity;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "facebook_users")
@Data
public class FacebookUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long appUserId; 

    private String facebookUserId; 

    @Column(columnDefinition = "TEXT")
    private String accessToken; 

    private Long expirySeconds;

    private LocalDateTime lastUpdated;

    private LocalDateTime expiryTime;
}
