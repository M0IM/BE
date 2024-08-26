package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.Alarm;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.UserReview;
import com.dev.moim.domain.account.repository.AlarmRepository;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.repository.UserReviewRepository;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.user.dto.UserDailyPlanPageDTO;
import com.dev.moim.domain.user.dto.UserPlanDTO;
import com.dev.moim.domain.user.dto.*;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.IndividualPlanException;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PostException;
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
import java.util.stream.Stream;

import static com.dev.moim.domain.account.entity.enums.ProfileType.MAIN;
import static com.dev.moim.domain.moim.entity.enums.MoimRole.OWNER;
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
    private final AlarmRepository alarmRepository;
    private final UserPlanRepository userPlanRepository;
    private final MoimRepository moimRepository;
    private final PostRepository postRepository;

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
    public UserDailyPlanPageDTO getUserDailyPlanList(User user, int year, int month, int day, int page, int size) {
        LocalDateTime startOfDay = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        int offset = (page-1) * size;
        List<Object[]> userDailyPlanList = planRepository.findUserPlansAndIndividualPlans(user.getId(), startOfDay, endOfDay, size+1, offset);
        boolean hasNext = userDailyPlanList.size() > size;
        List<Object[]> finalList = hasNext ? userDailyPlanList.subList(0, size) : userDailyPlanList;
        boolean isFirst = page == 1;

        return UserDailyPlanPageDTO.toUserDailyPlan(finalList, isFirst, hasNext);
    }

    @Override
    public PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<IndividualPlan> individualPlanList = individualPlanRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate);

        Map<Integer, List<UserPlanDTO>> monthPlanListByDay = individualPlanList.stream()
                .map(UserPlanDTO::toIndividualPlan)
                .collect(Collectors.groupingBy(dto -> dto.time().getDayOfMonth()));

        return new PlanMonthListDTO<>(monthPlanListByDay);
    }

    @Override
    public PlanMonthListDTO<List<UserPlanDTO>> getUserMonthlyPlans(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<UserPlanDTO> userMoimPlanDTOList = userPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate).stream()
                .map(userPlan -> UserPlanDTO.toUserMoimPlan(userPlan.getPlan()))
                .toList();

        List<UserPlanDTO> individualPlanDTOList = individualPlanRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate).stream()
                .map(UserPlanDTO::toIndividualPlan)
                .toList();

        List<UserPlanDTO> userMonthlyPlanDTOList = Stream.concat(userMoimPlanDTOList.stream(), individualPlanDTOList.stream())
                .toList();

        Map<Integer, List<UserPlanDTO>> userMonthlyPlanListByDay = userMonthlyPlanDTOList.stream()
                .collect(Collectors.groupingBy(plan -> plan.time().getDayOfMonth()));

        return new PlanMonthListDTO<>(userMonthlyPlanListByDay);
    }

    @Override
    public UserDailyPlanCntDTO getUserDailyPlanCnt(User user, int year, int month, int day) {
        LocalDateTime startOfDay = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        int individualPlanCnt = individualPlanRepository.countByUserAndDateBetween(user, startOfDay, endOfDay);
        int moimPlanCnt = userPlanRepository.countPlansByUserAndDateBetween(user, startOfDay, endOfDay);

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return new UserDailyPlanCntDTO(userProfile.getName(), individualPlanCnt + moimPlanCnt);
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

    @Override
    public boolean isMoimOwner(User user) {
        return userMoimRepository.existsByUserAndMoimRole(user, OWNER);
    }

    @Override
    public List<User> findAllUser() {return userRepository.findAll();}

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public AlarmResponseListDTO getAlarms(User user, Long cursor, Integer take) {
        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Alarm> alarmSlices = alarmRepository.findByUserAndIdLessThanOrderByIdDesc(user, cursor, PageRequest.of(0, take));

        List<AlarmResponseDTO> alarmResponseDTOList = alarmSlices.stream().map(AlarmResponseDTO::toAlarmResponseDTO
        ).toList();

        Long nextCursor = null;
        if (!alarmSlices.isLast()) {
            nextCursor = alarmSlices.toList().get(alarmSlices.toList().size() - 1).getId();
        }

        return AlarmResponseListDTO.toAlarmResponseListDTO(alarmResponseDTOList, nextCursor, alarmSlices.hasNext());
    }

    @Override
    public List<UserPreviewDTO> findUnReadUserListByPost(User user, Long moimId, Long postId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(MOIM_NOT_FOUND));
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostException(POST_NOT_FOUND));

        if (!post.getPostType().equals(PostType.ANNOUNCEMENT)) {
           throw new PostException(ErrorStatus.NOT_ANNOUNCEMENT_POST);
        }


        List<UserProfileDTO> readUsers = userRepository.findReadUsers(post);

        List<UserPreviewDTO> userPreviewDTOList = readUsers.stream().map(UserPreviewDTO::toUserPreviewDTO).toList();

        return userPreviewDTOList;
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
