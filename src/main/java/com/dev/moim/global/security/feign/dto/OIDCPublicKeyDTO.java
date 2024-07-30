package com.dev.moim.global.security.feign.dto;

public record OIDCPublicKeyDTO(
        String kid,
        String alg,
        String use,
        String n,
        String e
) {
}