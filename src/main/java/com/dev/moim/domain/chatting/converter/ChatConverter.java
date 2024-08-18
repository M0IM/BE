package com.dev.moim.domain.chatting.converter;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.chatting.dto.ChatDTO;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.global.socket.dto.ChattingDTO.MessageDTO;
import org.springframework.data.domain.Slice;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class ChatConverter {

    public static Chat toChat(MessageDTO messageDTO, User user, ChatRoom chatRoom) {
        return Chat.builder()
                .content(messageDTO.getContent())
                .imageKeyName(messageDTO.getImageKeyName())
                .user(user)
                .chatRoom(chatRoom)
                .build();
    }

    public static ChatDTO.ChatListResponse toChatResponseList(List<ChatDTO.ChatResponse> chatResponses, Long nextCursor, Boolean hasNext) {

        Collections.reverse(chatResponses);

        return ChatDTO.ChatListResponse.builder()
                .hasNext(hasNext)
                .chatResponseList(chatResponses)
                .nextCursor(nextCursor)
                .build();
    }

    public static ChatDTO.ChatResponse toChatResponse(Chat chat, UserProfile userProfile) {
        User user = chat.getUser();

        ChatDTO.SenderDTO senderDTO = ChatDTO.SenderDTO.builder()
                .senderId(user == null ? null : user.getId())
                .senderName(user == null ? "알 수 없음" : userProfile.getName())
                .senderProfile(user == null ? null : userProfile.getImageUrl())
                .build();

        return ChatDTO.ChatResponse.builder()
                .chatId(chat.getId())
                .senderDTO(senderDTO)
                .content(chat.getContent() == null ? null : chat.getContent())
                .imageKeyName(chat.getImageKeyName() == null ? null : chat.getImageKeyName())
                .createAt(chat.getCreatedAt())
                .build();
    }

}
