package com.dev.moim.global.security.feign.request;

import com.dev.moim.global.security.feign.config.NaverFeignConfiguration;
import com.dev.moim.global.security.dto.NaverUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "naverFeign",
        url = "https://openapi.naver.com",
        configuration = NaverFeignConfiguration.class
)
public interface NaverFeign {
    @GetMapping("/v1/nid/me")
    NaverUserInfo getUserInfo(@RequestHeader(name = "Authorization") String accessToken);
}
