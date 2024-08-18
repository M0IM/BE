package com.dev.moim.domain.chatting.repository;

import com.dev.moim.domain.chatting.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, CustomChatRoomRepository {
    Boolean existsChatRoomById(Long id);
}
