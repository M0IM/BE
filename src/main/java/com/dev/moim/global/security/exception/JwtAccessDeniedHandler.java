package com.dev.moim.global.security.exception;

import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.util.HttpResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_INSUFFICIENT_PERMISSION;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                USER_INSUFFICIENT_PERMISSION.getCode(),
                USER_INSUFFICIENT_PERMISSION.getMessage(),
                null);

        HttpResponseUtil.setErrorResponse(response, HttpStatus.FORBIDDEN, errorResponse);
    }
}
