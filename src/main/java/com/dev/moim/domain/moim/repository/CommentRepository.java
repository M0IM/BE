package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findByPostAndIdGreaterThanAndParentIsNullOrderByIdAsc(Post post, Long id, Pageable pageable);

    @Query("select c from CommentBlock cb join cb.user u join cb.comment c where u = :user and c.post.id = :postId")
    List<Comment> findByUserAndPostId(User user, Long postId);

    @Query("select c from CommentBlock cb join cb.comment c where cb.user = :user")
    List<Comment> findByBlockComment(User user);
}
