package com.dev.moim.global.security.service;

import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.feign.config.OauthProperties;
import com.dev.moim.global.security.feign.dto.OIDCPublicKeyDTO;
import com.dev.moim.global.security.feign.dto.OIDCPublicKeyListDTO;
import com.dev.moim.global.security.feign.request.AppleFeign;
import com.dev.moim.global.security.feign.request.GoogleFeign;
import com.dev.moim.global.security.feign.request.KakaoFeign;
import com.dev.moim.global.security.util.JwtOIDCUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import static com.dev.moim.domain.account.entity.enums.Provider.*;
import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class OIDCService {

    private final KakaoFeign kakaoFeign;
    private final GoogleFeign googleFeign;
    private final AppleFeign appleFeign;
    private final JwtOIDCUtil jwtOIDCUtil;
    private final OauthProperties oauthProperties;
    private final CacheManager oidcCacheManager;

    public OIDCDecodePayload getOIDCDecodePayload(Provider provider, String idToken) {
        OIDCPublicKeyListDTO oidcPublicKeyList = getOidcPublicKeyList(provider);

        try {
            return getPayloadFromIdToken(
                    idToken,
                    oauthProperties.getBaseUrl(provider),
                    oauthProperties.getAppKey(provider),
                    oidcPublicKeyList);
        } catch (AuthException e) {
            if (e.getCode() == OIDC_PUBLIC_KEY_NOT_FOUND) {
                log.info("OIDC_PUBLIC_KEY_NOT_FOUND");

                invalidateCache(provider);

                oidcPublicKeyList = getOidcPublicKeyList(provider);

                return getPayloadFromIdToken(
                        idToken,
                        oauthProperties.getBaseUrl(provider),
                        oauthProperties.getAppKey(provider),
                        oidcPublicKeyList);
            }
            throw new AuthException((ErrorStatus) e.getCode());
        }
    }

    private OIDCPublicKeyListDTO getOidcPublicKeyList(Provider provider) {
        if (provider.equals(KAKAO)) {
            return kakaoFeign.getKakaoOIDCOpenKeys();
        } else if (provider.equals(GOOGLE)) {
            return googleFeign.getGoogleOIDCOpenKeys();
        } else if (provider.equals(APPLE)) {
            return appleFeign.getAppleOIDCOpenKeys();
        } else {
            throw new AuthException(PROVIDER_NOT_FOUND);
        }
    }

    private void invalidateCache(Provider provider) {
        Cache cache = oidcCacheManager.getCache(provider.toString());
        if (cache != null) {
            cache.clear();
            log.info("캐시된 {} 공개키 리스트 clear", provider);
        }
    }

    public OIDCDecodePayload getPayloadFromIdToken(String token, String iss, String aud, OIDCPublicKeyListDTO oidcPublicKeyList) {
        String kid = jwtOIDCUtil.getKidFromUnsignedTokenHeader(token, iss, aud);

        OIDCPublicKeyDTO oidcPublicKey = oidcPublicKeyList.keys().stream()
                .filter(o -> o.kid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new AuthException(OIDC_PUBLIC_KEY_NOT_FOUND));

        return jwtOIDCUtil.getOIDCTokenBody(
                token, oidcPublicKey.n(), oidcPublicKey.e());
    }
}
