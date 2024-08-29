package com.dev.moim.domain.user.service;

import com.dev.moim.domain.user.dto.ReviewListDTO;

public interface ReviewQueryService {

    ReviewListDTO getUserReviews(Long userId, int page, int size);
}
