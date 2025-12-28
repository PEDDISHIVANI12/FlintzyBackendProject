package com.flintzy.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flintzy.entity.FacebookPost;

@Repository
public interface FacebookPostRepo extends JpaRepository<FacebookPost, Long> {}

