package com.dev.moim.global.security.util;

import com.dev.moim.global.common.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setSuccessResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
            throws IOException {
        String responseBody = objectMapper.writeValueAsString(BaseResponse.onSuccess(body));
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(responseBody);
    }

    public static void setErrorResponse(HttpServletResponse response, HttpStatus httpStatus, Object body)
            throws IOException {
        response.setContentType("application/json");
        response.setStatus(httpStatus.value());
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
