package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.security.service.OIDCService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class OAuthAuthenticationProvider implements AuthenticationProvider {

    private final OIDCService oidcService;

    public OAuthAuthenticationProvider(OIDCService oidcService) {
        this.oidcService = oidcService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OAuthAuthenticationToken oAuthToken = (OAuthAuthenticationToken) authentication;
        Provider provider = oAuthToken.getProvider();
        String idToken = oAuthToken.getIdToken();

        OIDCDecodePayload oidcDecodePayload = oidcService.getOIDCDecodePayload(provider, idToken);

        return new OAuthAuthenticationToken(provider, oidcDecodePayload.sub(), idToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OAuthAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

