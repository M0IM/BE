package com.dev.moim.global.security.service;

import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.entity.enums.Provider;
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
import org.springframework.stereotype.Service;

import static com.dev.moim.domain.account.entity.enums.Provider.*;
import static com.dev.moim.global.common.code.status.ErrorStatus.OAUTH_PROVIDER_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Slf4j
public class OIDCService {

    private final KakaoFeign kakaoFeign;
    private final GoogleFeign googleFeign;
    private final AppleFeign appleFeign;
    private final JwtOIDCUtil jwtOIDCUtil;
    private final OauthProperties oauthProperties;

    public OIDCDecodePayload getOIDCDecodePayload(Provider provider, String idToken) {
        OIDCPublicKeyListDTO oidcPublicKeyList;

        if (provider.equals(KAKAO)) {
            oidcPublicKeyList = kakaoFeign.getKakaoOIDCOpenKeys();
        } else if (provider.equals(GOOGLE)) {
            oidcPublicKeyList = googleFeign.getGoogleOIDCOpenKeys();
        } else if (provider.equals(APPLE)) {
            oidcPublicKeyList = appleFeign.getAppleOIDCOpenKeys();
        }else {
            throw new AuthException(OAUTH_PROVIDER_NOT_FOUND);
        }

        return getPayloadFromIdToken(
                idToken,
                oauthProperties.getBaseUrl(provider),
                oauthProperties.getAppKey(provider),
                oidcPublicKeyList);
    }

    public OIDCDecodePayload getPayloadFromIdToken(String token, String iss, String aud, OIDCPublicKeyListDTO oidcPublicKeyList) {
        String kid = jwtOIDCUtil.getKidFromUnsignedTokenHeader(token, iss, aud);

        OIDCPublicKeyDTO oidcPublicKey = oidcPublicKeyList.keys().stream()
                .filter(o -> o.kid().equals(kid))
                .findFirst()
                .orElseThrow();

        return jwtOIDCUtil.getOIDCTokenBody(
                token, oidcPublicKey.n(), oidcPublicKey.e());
    }
}
