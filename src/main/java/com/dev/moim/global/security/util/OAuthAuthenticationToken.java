package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.entity.enums.Provider;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class OAuthAuthenticationToken extends AbstractAuthenticationToken {
    private final Provider provider;
    private final String providerId;

    public OAuthAuthenticationToken(Provider provider, String providerId) {
        super(null);
        this.provider = provider;
        this.providerId = providerId;
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

