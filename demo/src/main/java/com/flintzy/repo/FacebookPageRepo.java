package com.flintzy.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flintzy.entity.FacebookPage;

@Repository
public interface FacebookPageRepo extends JpaRepository<FacebookPage, String> {
	Optional<FacebookPage> findByPageId(String pageId);

    List<FacebookPage> findByAppUserId(String userId);
	
}

