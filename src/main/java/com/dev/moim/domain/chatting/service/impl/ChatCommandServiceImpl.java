package com.dev.moim.domain.chatting.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.chatting.converter.ChatConverter;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRepository;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.chatting.service.ChatCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.socket.dto.ChattingDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatCommandServiceImpl implements ChatCommandService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Chat saveChat(ChattingDTO.MessageDTO messageDTO, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageDTO.getChatRoomId()).orElseThrow(()-> new ChatRoomException(ErrorStatus.CHATROOM_NOT_FOUND));

        User user = userRepository.findById(userId).orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));

        return chatRepository.save(ChatConverter.toChat(messageDTO, user, chatRoom));
    }
}
