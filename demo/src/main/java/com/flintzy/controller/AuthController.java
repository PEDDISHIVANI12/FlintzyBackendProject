package com.flintzy.controller;

import com.flintzy.config.JwtUtil;
import com.flintzy.dto.JwtResponse;
import com.flintzy.entity.User;
import com.flintzy.repo.UserRepo;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserRepo userRepo;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/oauth-success")
    public Map<String, String> oauthSuccess(@AuthenticationPrincipal OAuth2User user) {

        String email = user.getAttribute("email");

        String token = jwtUtil.generateToken(email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "OAuth login successful");
        response.put("email", email);
        response.put("jwt", token);

        return response;
    }

}
