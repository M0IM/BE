package com.dev.moim.global.socket.handler;


import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatException;
import com.dev.moim.global.socket.dto.ChattingDTO.MessageDTO;
import com.dev.moim.global.socket.service.SocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@RequiredArgsConstructor
@Slf4j
public class TextHandler extends TextWebSocketHandler {
    
    public static Map<Long, CopyOnWriteArraySet<WebSocketSession>> chatSessionRoom = new ConcurrentHashMap<>();
    private final SocketService socketService;
    private final SocketUtil socketUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        socketUtil.createOrJoinSessionChatRoom(0L, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        MessageDTO chattingDTO = parseMessage(message);

        switch (chattingDTO.getChatType()) {
            case ENTER:
                socketService.handleEnter(session, chattingDTO);
                break;

            case TALK:
                socketService.handleTalk(session, chattingDTO);
                break;

            case EXIT:
                socketService.handleExit(session, chattingDTO);
                break;

            default:
                throw new ChatException(ErrorStatus.INVALID_CHAT_FORMAT);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        session.sendMessage(new TextMessage("session error"));
        session.close();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        socketUtil.exitAllSessionChatRoom(session);
        log.info("status : " + status.getReason());
    }

    private MessageDTO parseMessage(TextMessage message) {
        try {
            return objectMapper.readValue(message.getPayload(), MessageDTO.class);
        } catch (JsonProcessingException e) {
            throw new ChatException(ErrorStatus.INVALID_CHAT_FORMAT);
        }
    }
}
