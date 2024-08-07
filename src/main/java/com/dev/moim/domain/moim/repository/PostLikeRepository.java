package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsPostLikeByUserIdAndPostId(Long userId, Long postId);
}
