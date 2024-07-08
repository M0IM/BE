package com.dev.moim.global.security.exception;

import com.dev.moim.global.common.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_INSUFFICIENT_PERMISSION;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(403);

        BaseResponse<Object> errorResponse = BaseResponse.onFailure(
                USER_INSUFFICIENT_PERMISSION.getCode(),
                USER_INSUFFICIENT_PERMISSION.getMessage(),
                null);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
