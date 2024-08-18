package com.dev.moim.global.socket.handler;


import com.dev.moim.global.security.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@RequiredArgsConstructor
public class JWTInterceptor implements HandshakeInterceptor {
    private final JwtUtil jwtUtil;
    private final SocketUtil socketUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        System.out.println(1);
        String jwtToken = servletRequest.getParameter("token");
        System.out.println(jwtToken);
            if (jwtToken != null && !jwtToken.isEmpty()) {
                try {
                    jwtUtil.isTokenValid(jwtToken);
                } catch (JwtException | IllegalArgumentException e) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }

                // JWT에서 이메일과 소셜 타입 추출
                String userId = jwtUtil.getUserId(jwtToken);

                // 이미 존재하는 소켓 체크
                if (socketUtil.allAlReadyExistsInAnyChatRoom(Long.valueOf(userId))) {
                    response.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
                    return false;
                }

                // 속성에 이메일과 소셜 타입 추가
                attributes.put("userId", userId);
                return true;
            }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
