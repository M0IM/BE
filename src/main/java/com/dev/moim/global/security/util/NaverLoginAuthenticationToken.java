package com.dev.moim.global.security.util;

import com.dev.moim.domain.account.entity.enums.Provider;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

@Getter
public class NaverLoginAuthenticationToken extends AbstractAuthenticationToken {

    private final Provider provider;
    private final String providerId;
    private final String oAuthAccessToken;
    private final String email;

    public NaverLoginAuthenticationToken(Provider provider, String providerId, String oAuthAccessToken, String email) {
        super(null);
        this.provider = provider;
        this.providerId = providerId;
        this.oAuthAccessToken = oAuthAccessToken;
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