package com.dev.moim.global.security.annotation;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.redis.util.RedisUtil;
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

import java.util.Date;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class) && parameter.getParameterType().equals(User.class);
    }

    @Override
    public Object resolveArgument(
            @Nonnull  MethodParameter parameter,
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
                    User user = userRepository.findById(Long.valueOf(userId))
                            .orElseThrow(() -> new AuthException(USER_NOT_FOUND));

                    if (user.getDeviceId() == null) {
                        Long now = new Date().getTime();
                        Long expiration = jwtUtil.getExpiration(accessToken) - now;
                        redisUtil.setValue(accessToken, "deviceId_missing", expiration);

                        throw new AuthException(FCM_TOKEN_REQUIRED);
                    }
                    return user;
                }).orElseThrow(() -> new AuthException(AUTH_INVALID_TOKEN));
    }
}
