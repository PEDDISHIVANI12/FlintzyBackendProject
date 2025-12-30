package com.flintzy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "facebook_pages")
public class FacebookPage {

    @Id
    private String pageId;
    private String pageName;
    @Column(columnDefinition = "TEXT")
    private String accessToken;   // ENCRYPTED Page Token
    private Long appUserId;       // Google user ID
    private String facebookUserId; // FB User ID
    private Long expiresIn;
    private LocalDateTime lastUpdated;
    private LocalDateTime expiryTime;
}
