package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.CommentBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentBlockRepository extends JpaRepository<CommentBlock, Long> {
    Optional<CommentBlock> findByUserAndComment(User user, Comment comment);
}
