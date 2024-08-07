package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.validation.annotation.LocalAccountValidation;
import com.dev.moim.global.validation.annotation.PasswordValidation;
import com.dev.moim.global.validation.annotation.OAuthAccountValidation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

@OAuthAccountValidation
@LocalAccountValidation
@PasswordValidation
public record JoinRequest(
        @Schema(description = "로그인 타입", defaultValue = "KAKAO", allowableValues = {"LOCAL", "KAKAO", "APPLE", "GOOGLE"})
        @NonNull Provider provider,
        String providerId,
        @NotBlank String nickname,
        @NotBlank String email,
        String password,
        @Schema(description = "성별", defaultValue = "FEMALE", allowableValues = {"FEMALE", "MALE"})
        @NonNull Gender gender,
        @NotBlank String birth,
        @NotBlank String residence
) {
}
