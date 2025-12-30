package com.flintzy.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.flintzy.entity.FacebookPage;
import com.flintzy.entity.FacebookUser;

@Service
public class FacebookTokenService {

	public boolean isUserTokenExpired(FacebookUser u) {
	    if (u.getExpiryTime() == null) return true;
	    return LocalDateTime.now().isAfter(u.getExpiryTime());
	}
}
