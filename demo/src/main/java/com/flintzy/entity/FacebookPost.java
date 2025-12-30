package com.flintzy.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "facebook_posts")
@Data
public class FacebookPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pageId;        
    private Long appUserId;  
    private String facebookUserId;
    private String facebookPostId; 
    private String caption;
    private String mediaType;
    private LocalDateTime createdAt;
}
