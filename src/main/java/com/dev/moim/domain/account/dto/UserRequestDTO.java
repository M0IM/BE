package com.dev.moim.domain.account.dto;

import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class PostReviewDTO {
        private Long reviewId;
        private String exitReason;
    }
}
