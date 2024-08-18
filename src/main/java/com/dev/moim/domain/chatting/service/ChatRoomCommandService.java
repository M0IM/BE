package com.dev.moim.domain.chatting.service;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO;
import com.dev.moim.domain.chatting.entity.ChatRoom;

public interface ChatRoomCommandService {
    ChatRoom createChatRoom(ChatRoomDTO.CreateChatRoomRequest createChatRoomRequest, User user);

    ChatRoom updateChatRoom(ChatRoomDTO.UpdateChatRoomRequest updateChatRoomRequest);

    void exitChatRoom(ChatRoomDTO.ExitChatRoomRequest exitChatRoomRequest, User user);

}
