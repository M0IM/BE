package com.dev.moim.global.security.principal;

import com.dev.moim.domain.moim.entity.enums.MoimRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public record MoimAuthentication(
        Authentication originalAuthentication,
        Long moimId,
        MoimRole moimRole,
        Long userMoimId) implements Authentication {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return originalAuthentication.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return originalAuthentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return originalAuthentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return originalAuthentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return originalAuthentication.isAuthenticated();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        originalAuthentication.setAuthenticated(isAuthenticated);
    }

    @Override
    public String getName() {
        return originalAuthentication.getName();
    }
}
