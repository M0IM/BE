package com.dev.moim.global.security.feign.request;

import com.dev.moim.global.security.feign.config.KakaoFeignConfiguration;
import com.dev.moim.global.security.feign.dto.OIDCPublicKeyListDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "KakaoFeign",
        url = "https://kauth.kakao.com",
        configuration = KakaoFeignConfiguration.class
)
public interface KakaoFeign {
    @Cacheable(cacheNames = "KAKAO", cacheManager = "oidcCacheManager")
    @GetMapping("/.well-known/jwks.json")
    OIDCPublicKeyListDTO getKakaoOIDCOpenKeys();
}
