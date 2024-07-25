package com.dev.moim.global.security.feign.dto;

import java.util.List;

public record OIDCPublicKeyListDTO (
        List<OIDCPublicKeyDTO> keys
) {
}
