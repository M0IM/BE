package com.dev.moim.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record JoinRequest(
        String nickname,
        String email,
        String password,
        @Schema(description = "유저 role", defaultValue = "ROLE_USER", allowableValues = {"ROLE_USER", "ROLE_ADMIN"})
        String role,
        @Schema(description = "성별", defaultValue = "FEMALE", allowableValues = {"FEMALE", "MALE"})
        String gender,
        int age,
        LocalDate birth,
        String residence,
        String introduction
) {
}
