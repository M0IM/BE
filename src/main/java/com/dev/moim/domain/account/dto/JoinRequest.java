package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Gender;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.validation.annotation.FcmTokenValidation;
import com.dev.moim.global.validation.annotation.LocalAccountValidation;
import com.dev.moim.global.validation.annotation.JoinPasswordValidation;
import com.dev.moim.global.validation.annotation.OAuthAccountValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

// @JoinFcmTokenValidtaion
@JsonInclude(JsonInclude.Include.NON_NULL)
@OAuthAccountValidation
@LocalAccountValidation
@JoinPasswordValidation
public record JoinRequest(
        @Schema(description = "로그인 타입", defaultValue = "KAKAO", allowableValues = {"LOCAL", "KAKAO", "APPLE", "GOOGLE"})
        @NonNull
        Provider provider,
        String providerId,
        @NotBlank String fcmToken,
        @NotBlank
        String nickname,
        @NotBlank
        String email,
        String password,
        @Schema(description = "성별", defaultValue = "FEMALE", allowableValues = {"FEMALE", "MALE"}, nullable = true)
        Gender gender,
        @Schema(nullable = true)
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birth,
        @Schema(nullable = true)
        String residence
) {
}
