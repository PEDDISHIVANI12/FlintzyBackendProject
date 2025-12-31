package com.flintzy.service;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.flintzy.entity.FacebookPage;
import com.flintzy.entity.FacebookPost;
import com.flintzy.entity.FacebookUser;
import com.flintzy.repo.FacebookPageRepo;
import com.flintzy.repo.FacebookPostRepo;
import com.flintzy.repo.FacebookUserRepo;
import com.flintzy.security.AESEncryptor;

@Service
public class FacebookPostingService {
	
	@Autowired
    private FacebookUserRepo facebookUserRepo;
    @Autowired
    private FacebookPageRepo pageRepo;

    @Autowired
    private FacebookPostRepo postRepo;
    
    @Autowired
    private FacebookTokenService tokenService;
 


    public Map<String, Object> publishTextPost(String pageId, String message) throws Exception {

        FacebookPage page = pageRepo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        FacebookUser fbUser = facebookUserRepo.findTopByFacebookUserIdOrderByLastUpdatedDesc(page.getFacebookUserId(),page.getAppUserId());

        if (tokenService.isUserTokenExpired(fbUser)) {
            throw new RuntimeException("Facebook session expired. Please login again.");
        }

        String token = AESEncryptor.decrypt(page.getPageAccessToken());
        RestTemplate rest = new RestTemplate();
        System.out.println(message);
        String url = "https://graph.facebook.com/" + pageId +
                "/feed?message=" + message +
                "&access_token=" + token;

        Map res = rest.postForObject(url, null, Map.class);

        FacebookPost post = new FacebookPost();
        post.setPageId(pageId);
        post.setFacebookUserId(page.getFacebookUserId());
        post.setAppUserId(page.getAppUserId());
        post.setCaption(message);
        post.setFacebookPostId((String) res.get("id"));
        post.setMediaType("TEXT");
        post.setCreatedAt(LocalDateTime.now());

        postRepo.save(post);

        return res;
    }



    public Map<String, Object> publishImagePost(String pageId, MultipartFile file, String caption) throws Exception {

        FacebookPage page = pageRepo.findById(pageId)
                .orElseThrow(() -> new RuntimeException("Page not found"));
       
        FacebookUser fbUser = facebookUserRepo.findTopByFacebookUserIdOrderByLastUpdatedDesc(page.getFacebookUserId(),page.getAppUserId());

        if (tokenService.isUserTokenExpired(fbUser)) {
            throw new RuntimeException("Facebook session expired. Please login again.");
        }
        String token = AESEncryptor.decrypt(page.getPageAccessToken());

        RestTemplate rest = new RestTemplate();

        String url = "https://graph.facebook.com/" + pageId + "/photos";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("caption", caption);
        body.add("access_token", token);
        body.add("source", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body);

        Map response = rest.postForObject(url, request, Map.class);

        FacebookPost post = new FacebookPost();
        post.setPageId(pageId);
        post.setAppUserId(page.getAppUserId());
        post.setFacebookUserId(page.getFacebookUserId());
        post.setCaption(caption);
        post.setFacebookPostId(response.get("id").toString());
        post.setMediaType("IMAGE");
        post.setCreatedAt(LocalDateTime.now());

        postRepo.save(post);

        return response;
    }
}

