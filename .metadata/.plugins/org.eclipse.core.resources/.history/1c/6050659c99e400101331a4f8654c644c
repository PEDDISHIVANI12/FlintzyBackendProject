package com.flintzy.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.flintzy.entity.FacebookPage;

@Service
public class FacebookTokenService {

    public boolean isTokenExpired(FacebookPage page) {

        if (page.getExpiresIn() == 0) {
            return false; 
        }

        LocalDateTime expiryTime =
                page.getLastUpdated().plusSeconds(page.getExpiresIn());

        return LocalDateTime.now().isAfter(expiryTime);
    }
}
