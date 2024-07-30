package com.dev.moim.global.security.util;

import com.dev.moim.global.error.handler.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

import static com.dev.moim.global.common.code.status.ErrorStatus._BAD_REQUEST;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T readBody(HttpServletRequest request, Class<T> requestDTO) {
        try {
            return objectMapper.readValue(request.getInputStream(), requestDTO);
        } catch (IOException e) {
            throw new AuthException(_BAD_REQUEST);
        }
    }

    public static String readHeader(HttpServletRequest request, String headerName) {
        String headerValue = request.getHeader(headerName);
        if (headerValue == null || headerValue.isEmpty()) {
            throw new AuthException(_BAD_REQUEST);
        }
        return headerValue;
    }
}
