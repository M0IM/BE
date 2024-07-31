package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.entity.enums.Provider;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class OIDCAuthenticationToken extends AbstractAuthenticationToken {
    private final Provider provider;
    private final String providerId;
    private final String idToken;

    public OIDCAuthenticationToken(Provider provider, String providerId, String idToken) {
        super(null);
        this.provider = provider;
        this.providerId = providerId;
        this.idToken = idToken;
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

