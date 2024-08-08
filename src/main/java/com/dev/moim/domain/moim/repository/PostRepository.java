package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.PostType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Slice<Post> findByMoimAndPostTypeAndIdLessThanOrderByIdDesc(Moim moim, PostType postType, Long id, Pageable pageable);
    Slice<Post> findByMoimAndIdLessThanOrderByIdDesc(Moim moim, Long id, Pageable pageable);
}
