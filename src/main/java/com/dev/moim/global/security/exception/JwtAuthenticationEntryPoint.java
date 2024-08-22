package com.dev.moim.global.security.exception;

import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_AUTHENTICATION_FAIL;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        log.error("** JwtAuthenticationEntryPoint **");

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                USER_AUTHENTICATION_FAIL.getCode(),
                USER_AUTHENTICATION_FAIL.getMessage(),
                null);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
    }
}
