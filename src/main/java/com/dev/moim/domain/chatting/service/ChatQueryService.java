package com.dev.moim.domain.chatting.service;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.chatting.dto.ChatDTO;

public interface ChatQueryService {
    ChatDTO.ChatListResponse getChats(User user, Long chatRoomId, Long cursor, Integer take);

}
