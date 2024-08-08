package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Boolean existsCommentLikeByUserIdAndCommentId(Long userId, Long commentId);
    Optional<CommentLike> findCommentLikeByUserIdAndCommentId(Long userId, Long commentId);
}
