package com.dev.moim.global.security.filter;

import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
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

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        log.info("** logoutFilter **");

        try {
            String accessToken = jwtUtil.resolveToken(request);

            Long now = new Date().getTime();
            Long expiration = jwtUtil.getExpiration(accessToken) - now;
            redisUtil.setValue(accessToken, "logout", expiration);

            redisUtil.deleteValue(jwtUtil.getEmail(accessToken));
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        }
    }
}