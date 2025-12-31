package com.flintzy.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.flintzy.entity.FacebookUser;

public interface FacebookUserRepo extends JpaRepository<FacebookUser, Long> {

    FacebookUser findByAppUserId(Long appUserId);

    FacebookUser findByFacebookUserId(String facebookUserId);
    
    FacebookUser findTopByFacebookUserIdOrderByLastUpdatedDesc(String facebookUserId, Long appUserId);
}
