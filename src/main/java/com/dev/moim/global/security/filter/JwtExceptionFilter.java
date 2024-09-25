package com.dev.moim.global.security.filter;

import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.error.handler.FeignException;
import com.dev.moim.global.util.HttpResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final String[] excludePath;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthException | FeignException e) {
            log.error("[ERROR] : {}", e.getCode());

            ErrorStatus errorStatus = (ErrorStatus) e.getCode();

            BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                    errorStatus.getCode(),
                    errorStatus.getMessage(),
                    null);

            HttpResponseUtil.setErrorResponse(response, e.getErrorReasonHttpStatus().getHttpStatus(), errorResponse);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }
}
