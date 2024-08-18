package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.PostBlock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostBlockRepository extends JpaRepository<PostBlock, Long> {
    Optional<PostBlock> findByUserIdAndPostId(Long userId, Long postId);
}
