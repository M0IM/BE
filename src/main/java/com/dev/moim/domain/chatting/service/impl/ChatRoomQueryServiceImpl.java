package com.dev.moim.domain.chatting.service.impl;

import com.dev.moim.domain.chatting.converter.ChatRoomConverter;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO.ChatRoomResponseList;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRepository;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.chatting.service.ChatRoomQueryService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatRoomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;

    @Override
    public ChatRoomResponseList getChatRoomsByUserIdAndMoimId(Long userId, Long moimId, Long cursor, Integer take) {
        // 가족 공간은 유무는 어노테이션으로 처리
        
        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<ChatRoom> chatRoomSlice = chatRoomRepository.getChatRoomsByUserIdAndMoimId(userId, moimId, cursor, take);
        Long nextCursor = null;
        if (!chatRoomSlice.isLast()) {
            nextCursor = findNextCursorByChatRoom(chatRoomSlice.toList().get(chatRoomSlice.toList().size() - 1));
        }
        return ChatRoomConverter.toChatRoomResponseList(chatRoomSlice, nextCursor, userId);
    }

    @Override
    public Optional<ChatRoom> findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId);
    }

    private Long findNextCursorByChatRoom(ChatRoom chatRoom) {
        Chat chat = chatRepository.findTopByChatRoomIdOrderByIdDesc(chatRoom.getId()).orElseThrow(()-> new ChatRoomException(ErrorStatus.INVALID_CHATROOM));
        return chat.getId();
    }
}
