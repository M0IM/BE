package com.dev.moim.domain.account.repository;

import com.dev.moim.domain.account.entity.UserReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {

    Page<UserReview> findByUserId(Long userId, Pageable pageRequest);
}
