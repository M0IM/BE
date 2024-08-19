package com.dev.moim.domain.chatting.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.chatting.converter.ChatConverter;
import com.dev.moim.domain.chatting.dto.ChatDTO;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRepository;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.chatting.repository.UserChatRoomRepository;
import com.dev.moim.domain.chatting.service.ChatQueryService;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.MoimException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService {

    private final ChatRepository chatRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public ChatDTO.ChatListResponse getChats(User user, Long chatRoomId, Long cursor, Integer take) {

        if (!userChatRoomRepository.existsByChatRoomIdAndUserId(chatRoomId, user.getId())) {
            throw new ChatRoomException(ErrorStatus.NOT_JOIN_CHATROOM);
        }

        // user와 chatRoom은 어노테이션으로 확인
        Slice<Chat> chatSlice;
        if (cursor == 1) {
            chatSlice = chatRepository.findChatsByLastReadTime(user.getId(), chatRoomId);
        } else {
            chatSlice = chatRepository.findChatByChatRoomIdOrderByDesc(cursor, take, chatRoomId);
        }

        Long nextCursor = null;
        if (!chatSlice.isLast()) {
            nextCursor = chatSlice.toList().get(chatSlice.toList().size() - 1).getId();
        }

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN).orElseThrow(() -> new MoimException(ErrorStatus.USER_PROFILE_NOT_FOUND));
        List<ChatDTO.ChatResponse> chatResponses = new java.util.ArrayList<>(chatSlice.stream().map((c) ->
                ChatConverter.toChatResponse(c, userProfile)
        ).toList());

        Collections.reverse(chatResponses);
        return ChatConverter.toChatResponseList(chatResponses, nextCursor, chatSlice.hasNext());
    }


}
