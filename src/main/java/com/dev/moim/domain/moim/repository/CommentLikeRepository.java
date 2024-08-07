package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsCommentLikeByUserIdAndCommentId(Long userId, Long commentId);
}
