package com.dev.moim.domain.chatting.repository;

import com.dev.moim.domain.chatting.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long>, CustomChatRepository {
    Optional<Chat> findTopByChatRoomIdOrderByIdDesc(Long chatRoomId);
}
