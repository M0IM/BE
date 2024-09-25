package com.dev.moim.global.security.feign.config;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.error.handler.AuthException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "oauth")
public class OauthProperties {

    private OAuthSecret kakao;
    private OAuthSecret google;
    private OAuthSecret apple;

    @Getter
    @Setter
    public static class OAuthSecret {
        private String baseUrl;
        private String appKey;
    }

    public String getBaseUrl(Provider provider) {
        return switch (provider) {
            case KAKAO -> getOAuthSecret(kakao).getBaseUrl();
            case GOOGLE -> getOAuthSecret(google).getBaseUrl();
            case APPLE -> getOAuthSecret(apple).getBaseUrl();
            default -> throw new AuthException(PROVIDER_NOT_FOUND);
        };
    }

    public String getAppKey(Provider provider) {
        return switch (provider) {
            case KAKAO -> getOAuthSecret(kakao).getAppKey();
            case GOOGLE -> getOAuthSecret(google).getAppKey();
            case APPLE -> getOAuthSecret(apple).getAppKey();
            default -> throw new AuthException(PROVIDER_NOT_FOUND);
        };
    }

    private OAuthSecret getOAuthSecret(OAuthSecret secret) {
        if (secret == null || secret.getBaseUrl() == null || secret.getAppKey() == null) {
            log.error("OAuth 관련 환경 변수 누락");
            throw new AuthException(OAUTH_SECRET_NOT_FOUND);
        }
        return secret;
    }
}