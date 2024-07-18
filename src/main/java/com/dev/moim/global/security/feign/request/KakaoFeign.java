package com.dev.moim.global.security.feign.request;

import com.dev.moim.global.security.feign.config.KakaoFeignConfiguration;
import com.dev.moim.global.security.feign.dto.KakaoUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "KakaoFeign",
        url = "https://kapi.kakao.com",
        configuration = KakaoFeignConfiguration.class
)
@Component
public interface KakaoFeign {
    @GetMapping(value = "/v2/user/me")
    KakaoUserInfo getUserInfo(@RequestHeader(name = "Authorization") String token);
}
