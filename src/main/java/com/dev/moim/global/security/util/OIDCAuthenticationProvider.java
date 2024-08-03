package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.dto.OIDCDecodePayload;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.security.service.OIDCService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class OIDCAuthenticationProvider implements AuthenticationProvider {

    private final OIDCService oidcService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        OIDCAuthenticationToken oidcAuthenticationToken = (OIDCAuthenticationToken) authentication;

        Provider provider = oidcAuthenticationToken.getPrincipal();
        String idToken = oidcAuthenticationToken.getIdToken();

        OIDCDecodePayload oidcDecodePayload = oidcService.getOIDCDecodePayload(provider, idToken);

        return new OIDCAuthenticationToken(provider, oidcDecodePayload.sub(), idToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OIDCAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

