package com.dev.moim.global.security.annotation;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.error.handler.AuthException;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.dev.moim.global.common.code.status.ErrorStatus.AUTH_INVALID_TOKEN;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            String userId = authentication.getName();
            return userRepository.findById(Long.valueOf(userId))
                    .orElseThrow(() -> new AuthException(AUTH_INVALID_TOKEN));
        }
        return null;
    }
}
