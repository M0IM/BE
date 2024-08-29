package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.UserReview;
import com.dev.moim.domain.account.repository.UserReviewRepository;
import com.dev.moim.domain.user.dto.ReviewListDTO;
import com.dev.moim.domain.user.service.ReviewQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewQueryServiceImpl implements ReviewQueryService {

    private final UserReviewRepository userReviewRepository;

    @Override
    public ReviewListDTO getUserReviews(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserReview> userReviewPage = userReviewRepository.findByUserId(userId, pageRequest);

        return ReviewListDTO.of(userReviewPage);
    }

}
