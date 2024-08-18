package com.dev.moim.global.socket.handler;


import com.dev.moim.domain.chatting.dto.ChatDTO;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatException;
import com.dev.moim.global.socket.dto.ChattingDTO;
import com.dev.moim.global.socket.dto.ChattingDTO.MessageDTO;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static com.dev.moim.global.socket.handler.TextHandler.chatSessionRoom;

@Component
public class SocketUtil {

    public void createOrJoinSessionChatRoom(Long roomId, WebSocketSession session) {
        if (existSessionChatRoom(roomId)) {
            joinSessionChatRoom(roomId, session);
        } else {
            createSessionChatRoom(roomId, session);
        }
    }



    public Boolean existSessionChatRoom(Long sessionChatRoomId) {
        return chatSessionRoom.containsKey(sessionChatRoomId);
    }


    public void createSessionChatRoom(Long sessionChatRoomId, WebSocketSession webSocketSession) {
        CopyOnWriteArraySet<WebSocketSession> webSocketSessions = new CopyOnWriteArraySet<>();
        webSocketSessions.add(webSocketSession);
        chatSessionRoom.put(sessionChatRoomId, webSocketSessions);
    }


    public void joinSessionChatRoom(Long sessionChatRoomId, WebSocketSession webSocketSession) {
        chatSessionRoom.get(sessionChatRoomId).add(webSocketSession);
    }


    public void sendToUserWithOutMe(WebSocketSession ownSession, MessageDTO messageDTO, ChatDTO.ChatResponse chatResponse) {
        for (WebSocketSession s : chatSessionRoom.get(messageDTO.getChatRoomId())) {
            if (!s.equals(ownSession)) {
                try {
                    s.sendMessage(new TextMessage(toChatMessageConverter(chatResponse)));
                } catch (IOException e) {
                    throw new ChatException(ErrorStatus.CHAT_NOT_SEND);
                }
            }
        }
    }

    public void sendToUserWithOutInSessionRoom(List<Long> ourUserInfo, ChatDTO.ChatResponse chatResponse) {
        for (WebSocketSession s : chatSessionRoom.get(0L)) {
            String userId = s.getAttributes().get("userId").toString();

            Boolean userExists = ourUserInfo.stream().anyMatch(u -> u.equals(Long.valueOf(userId)));

            if (userExists) {
                try {
                    s.sendMessage(new TextMessage(toChatMessageConverter(chatResponse)));
                } catch (IOException e) {
                    throw new ChatException(ErrorStatus.CHAT_NOT_SEND);
                }
            }
        }
    }

    public Set<Long> sendPushNotification(List<Long> ourUserInfo, ChattingDTO.MessageDTO chattingDTO) {
        Set<WebSocketSession> webSocketSessions = new CopyOnWriteArraySet<>();
        webSocketSessions.addAll(chatSessionRoom.get(0L));
        webSocketSessions.addAll(chatSessionRoom.get(chattingDTO.getChatRoomId()));

        return ourUserInfo.stream()
                .filter(usersId -> {
                    for (WebSocketSession s : webSocketSessions) {
                        if (usersId.equals(Long.valueOf(s.getAttributes().get("userId").toString()))) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toSet());
    }



    public void exitAllSessionChatRoom(WebSocketSession ownSession) {
        chatSessionRoom.forEach((l, s) -> {
            s.remove(ownSession);
        });
    }


    public Boolean alReadyJoinChatRoom(Long sessionChatRoomId, WebSocketSession webSocketSession) {
        return chatSessionRoom.get(sessionChatRoomId).contains(webSocketSession);
    }

    public Boolean existAllSessionChatRoom(WebSocketSession ownSession) {
        Boolean isTrue = false;
        for (Map.Entry<Long, CopyOnWriteArraySet<WebSocketSession>> entry : chatSessionRoom.entrySet()) {
            Long key = entry.getKey();
            CopyOnWriteArraySet<WebSocketSession> sessions = entry.getValue();

            if (key != 0L && sessions.contains(ownSession)) {
               isTrue = true;
               break;
            }
        }
        return isTrue;
    }

    public Boolean allAlReadyExistsInAnyChatRoom(Long userId) {
        for (CopyOnWriteArraySet<WebSocketSession> sessions : chatSessionRoom.values()) {
            for (WebSocketSession session : sessions) {
                Long sessionUserId = Long.valueOf(session.getAttributes().get("userId").toString());
                if (userId.equals(sessionUserId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String toChatMessageConverter(ChatDTO.ChatResponse chatResponse) {
        return String.format("{\"content\": \"%s\", \"imageKeyName\": \"%s\", \"senderDTO\": {\"senderId\": %d, \"senderName\": \"%s\", \"senderProfile\": \"%s\"}}",
                chatResponse.getContent(), chatResponse.getImageKeyName(), chatResponse.getSenderDTO().getSenderId(), chatResponse.getSenderDTO().getSenderName(), chatResponse.getSenderDTO().getSenderProfile());

    }
}
