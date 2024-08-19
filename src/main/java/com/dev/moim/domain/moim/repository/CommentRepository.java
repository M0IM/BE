package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Slice<Comment> findByPostAndIdGreaterThanAndParentIsNullOrderByIdAsc(Post post, Long id, Pageable pageable);
}
