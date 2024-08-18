package com.dev.moim.domain.chatting.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.chatting.converter.ChatRoomConverter;
import com.dev.moim.domain.chatting.converter.UserChatRoomConverter;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO;
import com.dev.moim.domain.chatting.dto.ChatRoomDTO.CreateChatRoomRequest;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.entity.UserChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRepository;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.chatting.repository.UserChatRoomRepository;
import com.dev.moim.domain.chatting.service.ChatRoomCommandService;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.GeneralException;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.UserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomCommandServiceImpl implements ChatRoomCommandService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatRepository chatRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserMoimRepository userMoimRepository;
    private final MoimRepository moimRepository;


    @Override
    public ChatRoom createChatRoom(CreateChatRoomRequest createChatRoomRequest, User user) {

        if (createChatRoomRequest.getRoomParticipantIds().contains(user.getId())) {
            throw new ChatRoomException(ErrorStatus.OVERLAP_JOIN_USER);
        }

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoomConverter.toChatRoom(createChatRoomRequest);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        createChatRoomRequest.getRoomParticipantIds().add(user.getId());
        createChatRoomRequest.getRoomParticipantIds().forEach((i)->{
           User chatUser = userRepository.findById(i).orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));
           userChatRoomRepository.save(UserChatRoomConverter.toUserChatRoom(savedChatRoom, chatUser));
        });

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN).orElseThrow(() -> new GeneralException(ErrorStatus.USER_PROFILE_NOT_FOUND));

        // 초기 chat 생성
        Chat createChat = Chat.builder()
                                .content(userProfile.getName() + "님이 채팅 방을 생성 하였 습니다.")
                                .user(user)
                                .chatRoom(savedChatRoom)
                                .build();

        chatRepository.save(createChat);

        return chatRoom;
    }

    @Override
    public ChatRoom updateChatRoom(ChatRoomDTO.UpdateChatRoomRequest updateChatRoomRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(updateChatRoomRequest.getChatRoomId()).orElseThrow(()-> new ChatRoomException(ErrorStatus.CHATROOM_NOT_FOUND));
        chatRoom.updateChatRoom(updateChatRoomRequest);
        return chatRoom;
    }

    @Override
    public void exitChatRoom(ChatRoomDTO.ExitChatRoomRequest exitChatRoomRequest, User user) {
        UserChatRoom userChatRoom = userChatRoomRepository.findByChatRoomIdAndUserId(exitChatRoomRequest.getChatRoomId(),user.getId()).orElseThrow(()->{
            throw new ChatRoomException(ErrorStatus.FIRST_JOIN_CHATROOM);
        });
        userChatRoomRepository.delete(userChatRoom);
    }
}
