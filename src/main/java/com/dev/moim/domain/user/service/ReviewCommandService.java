package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.CreateReviewResultDTO;

public interface ReviewCommandService {

    CreateReviewResultDTO postMemberReview(User user, CreateReviewDTO request);
}
