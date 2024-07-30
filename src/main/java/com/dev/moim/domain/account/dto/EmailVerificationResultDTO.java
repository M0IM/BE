package com.dev.moim.domain.account.dto;

public record EmailVerificationResultDTO(
        String email,
        boolean isCodeValid
) {
}
