package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.PostType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p " +
            "WHERE p.moim = :moim " +
            "AND p.postType = :postType " +
            "AND p.id < :id " +
            "AND p NOT IN (SELECT pb.post FROM PostBlock pb WHERE pb.user = :user) " +
            "ORDER BY p.id DESC")
    Slice<Post> findByMoimAndPostTypeAndIdLessThanAndUserPostBlocksNotInOrderByIdDesc(Moim moim, PostType postType, Long id, User user, Pageable pageable);


    @Query("SELECT p FROM Post p " +
            "WHERE p.moim = :moim " +
            "AND p.id < :id " +
            "AND p NOT IN (SELECT pb.post FROM PostBlock pb WHERE pb.user = :user) " +
            "ORDER BY p.id DESC")
    Slice<Post> findByMoimAndIdLessThanAndUserPostBlocksNotInOrderByIdDesc(Moim moim, Long id, User user, Pageable pageable);

    List<Post> findByMoimAndPostType(Moim moim, PostType postType);

    Slice<Post> findByPostTypeAndIdLessThanOrderByIdDesc(PostType postType, Long id, Pageable pageable);

    @Query("select p from Post p join p.moim m where m = :moim and p.postType != :postType")
    List<Post> findByNotPostTypeAndMoimOrderByCreatedAtDesc(PostType postType, Moim moim, Pageable pageable);
}
