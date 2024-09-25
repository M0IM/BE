package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.ReadPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReadPostRepository extends JpaRepository<ReadPost, Long> {
    Optional<ReadPost> findByUserAndPost(User user, Post post);
}
