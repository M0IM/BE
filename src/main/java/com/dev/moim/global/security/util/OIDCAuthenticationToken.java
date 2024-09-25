package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.entity.enums.Provider;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class OIDCAuthenticationToken extends AbstractAuthenticationToken {

    private final Provider provider;
    private final String providerId;
    private final String idToken;
    private final String email;

    public OIDCAuthenticationToken(Provider provider, String providerId, String idToken, String email) {
        super(null);
        this.provider = provider;
        this.providerId = providerId;
        this.idToken = idToken;
        this.email = email;
        setAuthenticated(false);
    }

    @Override
    public String getName() {
        return email;
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

