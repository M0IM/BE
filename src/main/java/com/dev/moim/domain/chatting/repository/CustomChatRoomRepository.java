package com.dev.moim.domain.chatting.repository;


import com.dev.moim.domain.chatting.entity.ChatRoom;
import org.springframework.data.domain.Slice;

public interface CustomChatRoomRepository {

    Slice<ChatRoom> getChatRoomsByUserIdAndMoimId(Long userId, Long moimId, Long cursor, Integer take);
}
