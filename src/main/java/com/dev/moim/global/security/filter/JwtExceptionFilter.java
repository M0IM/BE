package com.dev.moim.global.security.filter;

import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.security.util.HttpResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            log.info("** JwtExceptionFilter **");
            filterChain.doFilter(request, response);
        } catch (AuthException e) {
            ErrorStatus code = (ErrorStatus) e.getCode();

            BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                            code.getCode(),
                            code.getMessage(),
                            e.getMessage());

            HttpResponseUtil.setErrorResponse(response, e.getErrorReasonHttpStatus().getHttpStatus(), errorResponse);
        }
    }
}
