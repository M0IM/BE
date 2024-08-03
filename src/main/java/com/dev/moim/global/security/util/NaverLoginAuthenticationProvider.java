package com.dev.moim.global.security.util;

import com.dev.moim.global.error.handler.FeignException;
import com.dev.moim.global.security.dto.NaverUserInfo;
import com.dev.moim.global.security.service.NaverLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import static com.dev.moim.domain.account.entity.enums.Provider.NAVER;

@Slf4j
@RequiredArgsConstructor
@Component
public class NaverLoginAuthenticationProvider implements AuthenticationProvider {

    private final NaverLoginService naverLoginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException, FeignException {

        NaverLoginAuthenticationToken naverAuthenticationToken = (NaverLoginAuthenticationToken) authentication;

        NaverUserInfo naverUserInfo = naverLoginService.getUserInfo(naverAuthenticationToken.getOAuthAccessToken());

        return new NaverLoginAuthenticationToken(NAVER, naverUserInfo.getResponse().getId(), naverAuthenticationToken.getOAuthAccessToken());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return NaverLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
