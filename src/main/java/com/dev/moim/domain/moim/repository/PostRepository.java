package com.dev.moim.domain.moim.repository;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.enums.PostType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.userMoim um " +
            "WHERE p.moim = :moim " +
            "AND p.postType = :postType " +
            "AND p.id < :id " +
            "AND p NOT IN (SELECT pb.post FROM PostBlock pb WHERE pb.user = :user) " +
            "ORDER BY p.id DESC")
    Slice<Post> findByMoimAndPostTypeAndIdLessThanAndUserPostBlocksNotInOrderByIdDescWithUserMoim(
            @Param("moim") Moim moim,
            @Param("postType") PostType postType,
            @Param("id") Long id,
            @Param("user") User user,
            Pageable pageable);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.userMoim um " +
            "WHERE p.moim = :moim " +
            "AND p.postType != 'GLOBAL' " +
            "AND p.id < :id " +
            "AND p NOT IN (SELECT pb.post FROM PostBlock pb WHERE pb.user = :user) " +
            "ORDER BY p.id DESC")
    Slice<Post> findByMoimAndIdLessThanAndUserPostBlocksNotInOrderByIdDescWithUserMoim(
            @Param("moim") Moim moim,
            @Param("id") Long id,
            @Param("user") User user,
            Pageable pageable);

    List<Post> findByMoimAndPostType(Moim moim, PostType postType);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.userMoim um " +
            "WHERE p.postType = :postType " +
            "AND p.id < :id " +
            "ORDER BY p.id DESC")
    Slice<Post> findByPostTypeAndIdLessThanOrderByIdDescWithUserMoim(
            @Param("postType") PostType postType,
            @Param("id") Long id,
            Pageable pageable);

    @Query("select p from Post p " +
            "join fetch  p.moim m " +
            "left join fetch  p.userMoim um " +
            "where m = :moim and p.postType != :postType")
    List<Post> findByNotPostTypeAndMoimOrderByCreatedAtDescWithUserMoimAndMoim(PostType postType, Moim moim, Pageable pageable);

    @Query("select distinct p from Post p " +
            "join fetch p.userMoim " +
            "join fetch p.postBlockList pb " +
            "where pb.user = :user")
    List<Post> findBlockPostWithUserMoim(User user);

    @Query("SELECT p FROM Post p " +
            "LEFT JOIN FETCH p.userMoim um " +
            "LEFT JOIN FETCH p.postImageList pil " +
            "WHERE p.id = :id ")
    Optional<Post> findByIdWithUserMoimAndPostIIAndPostImageList(
            @Param("id") Long id);
}
