package com.dev.moim.domain.chatting.service.impl;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.chatting.entity.UserChatRoom;
import com.dev.moim.domain.chatting.repository.UserChatRoomRepository;
import com.dev.moim.domain.chatting.service.UserChatRoomCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserChatRoomCommandCommandServiceImpl implements UserChatRoomCommandService {
    private final UserRepository userRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    @Override
    public void updateLastTime(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));
        UserChatRoom userChatRoom = userChatRoomRepository.findByChatRoomIdAndUserId(chatRoomId, user.getId()).orElseThrow(()-> new ChatRoomException(ErrorStatus.FIRST_JOIN_CHATROOM));
        userChatRoom.updateLastReadTime();
    }
}
