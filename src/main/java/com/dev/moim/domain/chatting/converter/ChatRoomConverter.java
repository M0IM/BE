package com.dev.moim.domain.chatting.converter;

import com.dev.moim.domain.chatting.dto.ChatDTO;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO.CreateChatRoomResponse;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO.UpdateChatRoomResponse;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.moim.entity.Moim;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class ChatRoomConverter {

    public static ChatRoom toChatRoom(ChatRoomDTO.CreateChatRoomRequest createChatRoomRequest) {
        return ChatRoom.builder()
                .title(createChatRoomRequest.getTitle())
                .imageKeyName(createChatRoomRequest.getImageKeyName())
//                .moim(moim)
                .build();
    }

    public static CreateChatRoomResponse toCreateChatRoomResponse(ChatRoom chatRoom) {
        return CreateChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .createAt(chatRoom.getCreatedAt())
                .updateAt(chatRoom.getUpdatedAt())
                .build();
    }

    public static UpdateChatRoomResponse toUpdateChatRoomResponse(ChatRoom chatRoom) {
        return UpdateChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .updateAt(chatRoom.getUpdatedAt())
                .createAt(chatRoom.getCreatedAt())
                .build();
    }

    public static ChatRoomDTO.ChatRoomResponseList toChatRoomResponseList(Slice<ChatRoom> chatRoomList, Long nextCursor, Long userId) {
        List<ChatRoomDTO.ChatRoomResponse> chatRooms = chatRoomList
                .stream()
                .map(ChatRoomConverter::toChatRoomResponse)
                .collect(Collectors.toList());

        return ChatRoomDTO.ChatRoomResponseList.builder()
                .hasNext(chatRoomList.hasNext())
                .chatRoomResponses(chatRooms)
                .nextCursor(nextCursor)
                .ownerId(userId)
                .build();
    }

    private static ChatRoomDTO.ChatRoomResponse toChatRoomResponse(ChatRoom chatRoom) {
        return ChatRoomDTO.ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .title(chatRoom.getTitle())
                .imageKeyName(chatRoom.getImageKeyName())
                .build();
    }
}
