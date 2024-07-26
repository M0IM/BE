package com.dev.moim.global.security.feign.request;

import com.dev.moim.global.security.feign.config.AppleFeignConfiguration;
import com.dev.moim.global.security.feign.dto.OIDCPublicKeyListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "AppleFeign",
        url = "https://appleid.apple.com",
        configuration = AppleFeignConfiguration.class
)
public interface AppleFeign {
    @GetMapping("/auth/keys")
    OIDCPublicKeyListDTO getAppleOIDCOpenKeys();
}