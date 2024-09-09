package com.dev.moim.global.socket.service;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.chatting.converter.ChatConverter;
import com.dev.moim.domain.chatting.dto.ChatDTO;
import com.dev.moim.domain.chatting.entity.Chat;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.chatting.repository.UserChatRoomRepository;
import com.dev.moim.domain.chatting.service.ChatCommandService;
import com.dev.moim.domain.chatting.service.UserChatRoomCommandService;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatException;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.s3.service.S3Service;
import com.dev.moim.global.socket.dto.ChattingDTO;
import com.dev.moim.global.socket.handler.SocketUtil;
import com.dev.moim.global.socket.handler.TextHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketService {

    private final SocketUtil socketUtil;
    private final ChatCommandService chatCommandService;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final UserChatRoomCommandService userChatRoomCommandService;
    private final AlarmService alarmService;
    private final FcmService fcmService;
    private final UserMoimRepository userMoimRepository;
    private final UserProfileRepository userProfileRepository;
    private final S3Service s3Service;


    // scheduler 로 chatting room 연결이 끊길 시 연결 시키는 거 고려
    public void handleEnter(WebSocketSession session, ChattingDTO.MessageDTO chattingDTO) {
        User user = userRepository.findById(Long.valueOf(session.getAttributes().get("userId").toString())).orElseThrow(()-> new UserException(ErrorStatus.USER_NOT_FOUND));
        if (!chatRoomRepository.existsChatRoomById(chattingDTO.getChatRoomId())) {
            throw new ChatRoomException(ErrorStatus.CHATROOM_NOT_FOUND);
        }

        if (!userChatRoomRepository.existsByChatRoomIdAndUserId(chattingDTO.getChatRoomId(), user.getId())) {
            throw new ChatRoomException(ErrorStatus.NOT_JOIN_CHATROOM);
        }

        if (socketUtil.existAllSessionChatRoom(session)) {
            throw new ChatRoomException(ErrorStatus.ALREADY_JOIN_CHATROOM);
        }
        
        // 롤백 처리
        try {
            userChatRoomCommandService.updateLastTime(Long.valueOf(session.getAttributes().get("userId").toString()), chattingDTO.getChatRoomId());
            TextHandler.chatSessionRoom.get(0L).remove(session);
            socketUtil.createOrJoinSessionChatRoom(chattingDTO.getChatRoomId(), session);
        } catch (Exception e) {
            socketUtil.exitAllSessionChatRoom(session);
            socketUtil.createSessionChatRoom(0L, session);
            e.printStackTrace();
            throw new ChatRoomException(ErrorStatus.FAILED_ENTER_CHATROOM);
        }

        log.info("ChatSessionRoom 현황 : " + TextHandler.chatSessionRoom);
    }


    public void handleTalk(WebSocketSession session, ChattingDTO.MessageDTO chattingDTO) {
        if (!TextHandler.chatSessionRoom.containsKey(chattingDTO.getChatRoomId()) ||
                !TextHandler.chatSessionRoom.get(chattingDTO.getChatRoomId()).contains(session)) {
            throw new ChatRoomException(ErrorStatus.FIRST_JOIN_CHATROOM);
        }

        // 둘 중 하나는 NULL 이여야 함.
        if (chattingDTO.getContent() != null && chattingDTO.getImageKeyName() != null) {
            throw new ChatException(ErrorStatus.INVALID_CHAT_FORMAT);
        }

        if (chattingDTO.getImageKeyName()!= null) {
            String imageUrl = s3Service.generateStaticUrl(chattingDTO.getImageKeyName());
            chattingDTO.setImageKeyName(imageUrl);
        }



        // 채팅 방 번호로 해당 하는 채팅방 사람들의 이메일 받아오는 로직
        List<Long> userIds = userRepository.getUserIdByChatRoomId(chattingDTO.getChatRoomId());

        // 채팅 DB에 저장
        Chat chat = chatCommandService.saveChat(chattingDTO, Long.valueOf(session.getAttributes().get("userId").toString()));


        Optional<User> user = userRepository.findById(Long.valueOf(session.getAttributes().get("userId").toString()));

        // response 값 통일
        ChatRoom chatRoom = chatRoomRepository.findById(chattingDTO.getChatRoomId()).orElseThrow(() -> new ChatRoomException(ErrorStatus.CHATROOM_NOT_FOUND));
        User sender = userRepository.findById(Long.valueOf(session.getAttributes().get("userId").toString())).orElseThrow(()-> new ChatRoomException(ErrorStatus.USER_NOT_FOUND));
        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(sender.getId(), ProfileType.MAIN).orElseThrow(()->new ChatException(ErrorStatus.USER_PROFILE_NOT_FOUND));
        ChatDTO.ChatResponse chatResponse = ChatConverter.toChatResponse(chat, userProfile);

        // 메시지를 모든 세션에 전달
        socketUtil.sendToUserWithOutMe(session, chattingDTO, chatResponse);
        
        // 대기 방 인원에게 메시지 전달
        socketUtil.sendToUserWithOutInSessionRoom(userIds, chatResponse);
        
        // 밖 인원들 에게 푸시 알림 전달
        Set<Long> notEnterPeople = socketUtil.sendPushNotification(userIds, chattingDTO);

        notEnterPeople.forEach(p -> {
            User receiver = userRepository.findById(p).orElseThrow(()-> new ChatRoomException(ErrorStatus.USER_NOT_FOUND));
            if (receiver.getIsPushAlarm()) {
//                alarmService.saveAlarm(sender, receiver, chatRoom.getTitle(), chattingDTO.getContent(), AlarmType.PUSH, AlarmDetailType.CHATROOM, chatRoom.getId());
//                fcmService.sendNotification(receiver, chatRoom.getTitle(), chattingDTO.getContent());
            }
        });

        log.info("ChatSessionRoom 현황 : " + TextHandler.chatSessionRoom);
    }

    public void handleExit(WebSocketSession session, ChattingDTO.MessageDTO chattingDTO) {
        if (!socketUtil.existSessionChatRoom(chattingDTO.getChatRoomId()) ||
                !socketUtil.alReadyJoinChatRoom(chattingDTO.getChatRoomId(), session)) {
            throw new ChatException(ErrorStatus.FIRST_JOIN_CHATROOM);
        }

        // 롤백 처리
        try {
            userChatRoomCommandService.updateLastTime(Long.valueOf(session.getAttributes().get("userId").toString()), chattingDTO.getChatRoomId());
            TextHandler.chatSessionRoom.get(chattingDTO.getChatRoomId()).remove(session);
            TextHandler.chatSessionRoom.get(0L).add(session);
        } catch (Exception e) {
            socketUtil.exitAllSessionChatRoom(session);
            socketUtil.createSessionChatRoom(chattingDTO.getChatRoomId(), session);
            e.printStackTrace();
            throw new ChatRoomException(ErrorStatus.FAILED_EXIT_CHATROOM);
        }

        log.info("ChatSessionRoom 현황 : " + TextHandler.chatSessionRoom);
    }
}
