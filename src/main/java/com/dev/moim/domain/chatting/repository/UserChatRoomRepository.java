package com.dev.moim.domain.chatting.repository;


import com.dev.moim.domain.chatting.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom,Long> {
    Boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);
    Optional<UserChatRoom> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}
