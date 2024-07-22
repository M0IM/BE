package com.dev.moim.global.security.config;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.security.service.GoogleLoginService;
import com.dev.moim.global.security.service.KakaoLoginService;
import com.dev.moim.global.security.service.OAuthLoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.dev.moim.domain.account.entity.enums.Provider.GOOGLE;
import static com.dev.moim.domain.account.entity.enums.Provider.KAKAO;

@Configuration
public class OAuthLoginServiceConfig {

    @Bean
    public Map<Provider, OAuthLoginService> oAuthLoginServiceMap(
            KakaoLoginService kakaoLoginService,
            GoogleLoginService googleLoginService
    ) {
        Map<Provider, OAuthLoginService> map = new HashMap<>();
        map.put(KAKAO, kakaoLoginService);
        map.put(GOOGLE, googleLoginService);
        // map.put(APPLE, appleLoginService);
        return map;
    }
}