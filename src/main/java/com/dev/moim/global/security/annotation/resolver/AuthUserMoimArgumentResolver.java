package com.dev.moim.global.security.annotation.resolver;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.service.UserMoimQueryService;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
import com.dev.moim.global.security.annotation.annotation.AuthUserMoim;
import com.dev.moim.global.security.util.JwtUtil;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;
import static com.dev.moim.global.common.code.status.ErrorStatus.AUTH_INVALID_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUserMoimArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserMoimQueryService userMoimQueryService;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUserMoim.class) && parameter.getParameterType().equals(UserMoim.class);
    }

    @Override
    public Object resolveArgument(
            @Nonnull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @Nonnull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (httpServletRequest == null) {
            throw new AuthException(HTTP_REQUEST_NULL);
        }

        String accessToken = jwtUtil.resolveToken(httpServletRequest);

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(authentication -> {
                    String userId = authentication.getName();
                    Long moimId = extractMoimIdFromUri(httpServletRequest.getRequestURI());
                    UserMoim userMoim = userMoimQueryService.findByUserIdAndMoimIdAndJoinStatusWithUserAndMoim(
                            Long.valueOf(userId), moimId, JoinStatus.COMPLETE)
                            .orElseThrow(() -> new AuthException(USER_NOT_MOIM_JOIN));

                    if (userMoim.getUser().getDeviceId() == null) {
                        Long now = new Date().getTime();
                        Long expiration = jwtUtil.getExpiration(accessToken) - now;
                        redisUtil.setValue(accessToken, "deviceId_missing", expiration);

                        throw new AuthException(FCM_TOKEN_REQUIRED);
                    }

                    return userMoim;
                }).orElseThrow(() -> new AuthException(AUTH_INVALID_TOKEN));
    }

    private Long extractMoimIdFromUri(String uri) {
        return Arrays.stream(uri.split("/"))
                .sequential()
                .dropWhile(part -> !"moims".equals(part))
                .skip(1)
                .findFirst()
                .map(part -> {
                    try {
                        return Long.parseLong(part);
                    } catch (NumberFormatException e) {
                        throw new AuthException(INVALID_MOIM_MEMBER);
                    }
                })
                .orElseThrow(() -> new AuthException(MOIM_NOT_FOUND));
    }
}

