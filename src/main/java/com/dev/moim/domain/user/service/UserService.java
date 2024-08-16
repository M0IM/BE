package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;

public interface UserService {

    ProfileDTO getProfile(User user);

    ProfileDetailDTO getDetailProfile(Long userId);

    ReviewListDTO getUserReviews(Long userId, int page, int size);
}
