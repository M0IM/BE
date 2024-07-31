package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;

public record OAuthLoginRequest(
        @Schema(description = "소셜 로그인 타입", defaultValue = "KAKAO", allowableValues = {"KAKAO", "APPLE", "GOOGLE", "NAVER"})
        Provider provider,
        String token
) {
}