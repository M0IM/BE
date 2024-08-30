package com.dev.moim.global.security.filter;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.dev.moim.global.common.code.status.ErrorStatus.FCM_TOKEN_REQUIRED;
import static com.dev.moim.global.common.code.status.ErrorStatus.LOGOUT_ACCESS_TOKEN;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final String[] excludePath;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = jwtUtil.resolveToken(request);

        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (Objects.equals(redisUtil.getValue(accessToken), "deviceId_missing")) {
            filterChain.doFilter(request, response);
            throw new AuthException(FCM_TOKEN_REQUIRED);
        }

        if (Objects.equals(redisUtil.getValue(accessToken), "logout")) {
            filterChain.doFilter(request, response);
            throw new AuthException(LOGOUT_ACCESS_TOKEN);
        }

        if(jwtUtil.isTokenValid(accessToken)) {
            Authentication authentication = jwtUtil.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } else {
            throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
