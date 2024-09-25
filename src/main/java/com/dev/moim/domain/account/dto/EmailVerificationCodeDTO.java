package com.dev.moim.domain.account.dto;

public record EmailVerificationCodeDTO(
        String email,
        String code
) {
}
