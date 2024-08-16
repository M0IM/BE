package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.CreateReviewResultDTO;
import com.dev.moim.domain.user.dto.UpdateUserInfoDTO;

public interface UserCommandService {

    void updateInfo(User user, UpdateUserInfoDTO request);

    CreateReviewResultDTO postMemberReview(User user, CreateReviewDTO request);
}
