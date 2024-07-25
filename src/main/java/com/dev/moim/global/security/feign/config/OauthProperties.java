package com.dev.moim.global.security.feign.config;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.error.GeneralException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.dev.moim.global.common.code.status.ErrorStatus.OAUTH_PROVIDER_NOT_FOUND;

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
            default -> throw new GeneralException(OAUTH_PROVIDER_NOT_FOUND);
        };
    }

    public String getAppKey(Provider provider) {
        return switch (provider) {
            case KAKAO -> getOAuthSecret(kakao).getAppKey();
            case GOOGLE -> getOAuthSecret(google).getAppKey();
            case APPLE -> getOAuthSecret(apple).getAppKey();
            default -> throw new GeneralException(OAUTH_PROVIDER_NOT_FOUND);
        };
    }

    private OAuthSecret getOAuthSecret(OAuthSecret secret) {
        if (secret == null || secret.getBaseUrl() == null || secret.getAppKey() == null) {
            throw new GeneralException(OAUTH_PROVIDER_NOT_FOUND);
        }
        return secret;
    }
}
