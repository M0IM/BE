package com.dev.moim.domain.chatting.service;


import com.dev.moim.domain.account.entity.enums.Provider;

public interface UserChatRoomCommandService {
    void updateLastTime(Long userId, Long chatRoomId);
}
