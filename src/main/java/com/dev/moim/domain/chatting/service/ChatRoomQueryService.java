package com.dev.moim.domain.chatting.service;




import com.dev.moim.domain.chatting.dto.ChatRoomDTO;
import com.dev.moim.domain.chatting.entity.ChatRoom;

import java.util.Optional;

public interface ChatRoomQueryService {

    ChatRoomDTO.ChatRoomResponseList getChatRoomsByUserId(Long userId, Long cursor, Integer take);

    Optional<ChatRoom> findChatRoomById(Long chatRoomId);
}
