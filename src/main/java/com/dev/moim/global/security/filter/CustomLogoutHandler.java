package com.dev.moim.global.security.filter;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Date;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        try {
            String accessToken = jwtUtil.resolveToken(request);
            String userId = jwtUtil.getUserId(accessToken);

            User user = userQueryService.findUserById(Long.valueOf(userId))
                    .orElseThrow(() ->new AuthException(USER_NOT_FOUND));
            userCommandService.fcmSignOut(user);

            Long now = new Date().getTime();
            Long expiration = jwtUtil.getExpiration(accessToken) - now;
            redisUtil.setValue(accessToken, "logout", expiration);

            redisUtil.deleteValue(userId);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AUTH_EXPIRED_TOKEN);
        } catch (RedisConnectionFailureException e) {
            throw new AuthException(REDIS_CONNECTION_ERROR);
        }
    }
}