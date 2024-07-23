package com.dev.moim.global.security.filter;

import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.service.LogoutAccessTokenService;
import com.dev.moim.global.redis.service.RefreshTokenService;
import com.dev.moim.global.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Date;

import static com.dev.moim.global.common.code.status.ErrorStatus.AUTH_EXPIRED_TOKEN;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final LogoutAccessTokenService logoutAccessTokenService;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        log.info("** logoutFilter **");

        try {
            String accessToken = jwtUtil.resolveToken(request);

            log.info("accessToken = {}", accessToken);

            Long now = new Date().getTime();
            Long expiration = jwtUtil.getExpiration(accessToken) - now;
            logoutAccessTokenService.saveToken(accessToken, expiration);

            String email = jwtUtil.getEmail(accessToken);
            log.info("email = {}", email);
            refreshTokenService.deleteToken(email);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }
}