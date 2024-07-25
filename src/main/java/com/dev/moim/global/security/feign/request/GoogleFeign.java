package com.dev.moim.global.security.feign.request;

import com.dev.moim.global.security.feign.config.GoogleFeignConfiguration;
import com.dev.moim.global.security.feign.dto.OIDCPublicKeyListDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "GoogleFeign",
        url = "https://www.googleapis.com",
        configuration = GoogleFeignConfiguration.class
)
public interface GoogleFeign {
    @Cacheable(cacheNames = "GoogleOIDC", cacheManager = "oidcCacheManager")
    @GetMapping("/oauth2/v3/certs")
    OIDCPublicKeyListDTO getGoogleOIDCOpenKeys();
}
