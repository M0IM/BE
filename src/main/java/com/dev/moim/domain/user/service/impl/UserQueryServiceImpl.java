package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.UserReview;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.repository.UserReviewRepository;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.dto.calender.UserDailyPlanPageDTO;
import com.dev.moim.domain.moim.dto.calender.UserPlanDTO;
import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.entity.Plan;
import com.dev.moim.domain.moim.repository.IndividualPlanRepository;
import com.dev.moim.domain.moim.repository.PlanRepository;
import com.dev.moim.domain.chatting.entity.ChatRoom;
import com.dev.moim.domain.chatting.repository.ChatRoomRepository;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.user.dto.ChatRoomUserListResponse;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.error.handler.IndividualPlanException;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.ChatRoomException;
import com.dev.moim.global.error.handler.UserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dev.moim.domain.account.entity.enums.ProfileType.MAIN;
import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryServiceImpl implements UserQueryService {

    private final UserProfileRepository userProfileRepository;
    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    private final UserMoimRepository userMoimRepository;
    private final IndividualPlanRepository individualPlanRepository;
    private final PlanRepository planRepository;
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
        PageRequest pageRequest = PageRequest.of(page-1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserReview> userReviewPage = userReviewRepository.findByUserId(userId, pageRequest);

        return ReviewListDTO.of(userReviewPage);
    }

    @Override
    public UserDailyPlanPageDTO getUserDailyMoimPlan(User user, int year, int month, int day, int page, int size) {
        LocalDateTime startOfDay = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Pageable pageable = PageRequest.of(page-1, size);
        Slice<Plan> userMoimPlanSlice = planRepository.findByUserAndDateBetween(user, startOfDay, endOfDay, pageable);

        return UserDailyPlanPageDTO.toUserMoimPlan(userMoimPlanSlice);
    }

    @Override
    public UserDailyPlanPageDTO getUserDailyIndividualPlan(User user, int year, int month, int day, int page, int size) {
        LocalDateTime startOfDay = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.ASC, "date"));
        Slice<IndividualPlan> userIndividualPlanSlice = individualPlanRepository.findByUserAndDateBetween(user, startOfDay, endOfDay, pageable);

        return UserDailyPlanPageDTO.toUserIndividualPlan(userIndividualPlanSlice);
    }

    @Override
    public PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<IndividualPlan> individualPlanList = individualPlanRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        Map<Integer, List<UserPlanDTO>> monthPlanListByDay = individualPlanList.stream()
                .map(UserPlanDTO::of)
                .collect(Collectors.groupingBy(dto -> dto.time().getDayOfMonth()));

        return new PlanMonthListDTO<>(monthPlanListByDay);
    }

    @Override
    public List<Long> findUserMoimIdListByUserId(Long userId) {
        return userMoimRepository.findByUserId(userId).stream()
                .map(userMoim -> userMoim.getMoim().getId())
                .toList();
    }

    @Override
    public Long findUserByPlanId(Long individualPlanId) {
        IndividualPlan individualPlan = individualPlanRepository.findById(individualPlanId)
                .orElseThrow(() -> new IndividualPlanException(INDIVIDUAL_PLAN_NOT_FOUND));

        return individualPlan.getUser().getId();
    }

    @Override
    public boolean isExistEmail(String email) {
        return userRepository.existsByEmail(email);
    }
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
