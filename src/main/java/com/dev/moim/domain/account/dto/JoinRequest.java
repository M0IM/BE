package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.Provider;
import io.swagger.v3.oas.annotations.media.Schema;

public record JoinRequest(
        @Schema(description = "로그인 타입", defaultValue = "KAKAO", allowableValues = {"LOCAL", "KAKAO", "APPLE", "GOOGLE"})
        Provider provider,
        String providerId,
        String nickname,
        String email,
        String password,
        @Schema(description = "유저 role", defaultValue = "ROLE_USER", allowableValues = {"ROLE_USER", "ROLE_ADMIN"})
        String role,
        @Schema(description = "성별", defaultValue = "FEMALE", allowableValues = {"FEMALE", "MALE"})
        Gender gender,
        String birth,
        String residence
) {
}
