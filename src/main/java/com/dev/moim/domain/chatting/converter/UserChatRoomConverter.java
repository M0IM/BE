package com.dev.moim.domain.chatting.converter;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.entity.UserChatRoom;

import java.time.LocalDateTime;

public class UserChatRoomConverter {

    public static UserChatRoom toUserChatRoom (ChatRoom chatRoom, User user) {
        return UserChatRoom.builder()
                .chatRoom(chatRoom)
                .user(user)
                .lastReadTime(LocalDateTime.now())
                .build();
    }
}
