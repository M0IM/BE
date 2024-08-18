package com.dev.moim.global.socket;

import com.dev.moim.global.error.feign.service.DiscordClient;
import com.dev.moim.global.security.util.JwtUtil;
import com.dev.moim.global.socket.handler.CustomWebSocketExceptionHandler;
import com.dev.moim.global.socket.handler.JWTInterceptor;
import com.dev.moim.global.socket.handler.SocketUtil;
import com.dev.moim.global.socket.handler.TextHandler;
import com.dev.moim.global.socket.service.SocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final SocketService socketService;
    private final SocketUtil socketUtil;
    private final JwtUtil jwtUtil;
    private final DiscordClient discordClient;
    private final Environment environment;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new CustomWebSocketExceptionHandler(new TextHandler(socketService, socketUtil), discordClient, environment), "/chat")
                .addInterceptors(new JWTInterceptor(jwtUtil, socketUtil))
                .setAllowedOrigins("*");
    }
}
