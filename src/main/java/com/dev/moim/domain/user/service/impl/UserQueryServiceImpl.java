package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.UserReview;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.repository.UserReviewRepository;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.user.dto.ChatRoomUserListResponse;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.dev.moim.domain.account.entity.enums.ProfileType.MAIN;
import static com.dev.moim.global.common.code.status.ErrorStatus.USER_NOT_FOUND;
import static com.dev.moim.global.common.code.status.ErrorStatus.USER_PROFILE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserProfileRepository userProfileRepository;
    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    private final UserMoimRepository userMoimRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ProfileDTO getProfile(User user) {
        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return ProfileDTO.of(user, userProfile);
    }

    @Override
    public ProfileDetailDTO getDetailProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(userId, MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return ProfileDetailDTO.from(user, userProfile, userProfile.getImageUrl());
    }

    @Override
    public ReviewListDTO getUserReviews(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserReview> userReviewPage = userReviewRepository.findByUserId(userId, pageRequest);

        return ReviewListDTO.of(userReviewPage);
    }

    @Override
    public List<Long> findUserMoimIdListByUserId(Long userId) {
        return userMoimRepository.findByUserId(userId).stream()
                .map(userMoim -> userMoim.getMoim().getId())
                .toList();
    }

//    @Override
//    public ChatRoomUserListResponse getUserByChatRoom(User user, Long chatRoomId) {
//        if (!chatRoomRepository.existsChatRoomById(chatRoomId)) {
//            throw new ChatRoomException(ErrorStatus.CHATROOM_NOT_FOUND);
//        }
//
//        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ChatRoomException(ErrorStatus.CHATROOM_NOT_FOUND));
//
//        Optional<UserMoim> byUserAndMoim =  userMoimRepository.findByUserAndMoim(user, chatRoom.getMoim());
//
//
//
//        return ChatRoomUserListResponse(userByChatRoomId);
//    }
}