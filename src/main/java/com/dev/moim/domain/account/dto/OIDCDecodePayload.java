package com.dev.moim.domain.account.dto;

public record OIDCDecodePayload(
        String iss,
        String aud,
        String sub,
        String email,
        String nickname
) {
}
