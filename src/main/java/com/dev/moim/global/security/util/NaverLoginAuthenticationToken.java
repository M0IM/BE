package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.entity.enums.Provider;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class NaverLoginAuthenticationToken extends AbstractAuthenticationToken {
    private final Provider provider;
    private final String providerId;
    private final String oAuthAccessToken;

    public NaverLoginAuthenticationToken(Provider provider, String providerId, String oAuthAccessToken) {
        super(null);
        this.provider = provider;
        this.providerId = providerId;
        this.oAuthAccessToken = oAuthAccessToken;
        setAuthenticated(false);
    }

    @Override
    public Provider getPrincipal() {
        return provider;
    }

    @Override
    public String getCredentials() {
        return providerId;
    }
}