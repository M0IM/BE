package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.Alarm;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.account.repository.AlarmRepository;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.dto.MoimPreviewDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.impl.dto.UserProfileDTO;
import com.dev.moim.domain.user.dto.UserDailyPlanPageDTO;
import com.dev.moim.domain.user.dto.UserPlanDTO;
import com.dev.moim.domain.user.dto.*;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.*;
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
import java.util.Set;
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
    private final UserRepository userRepository;
    private final UserMoimRepository userMoimRepository;
    private final IndividualPlanRepository individualPlanRepository;
    private final PlanRepository planRepository;
    private final AlarmRepository alarmRepository;
    private final UserPlanRepository userPlanRepository;
    private final MoimRepository moimRepository;
    private final PostRepository postRepository;
    private final UserTodoRepository userTodoRepository;

    @Override
    public ProfileDTO getProfile(User user) {
        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return ProfileDTO.of(user, userProfile);
    }

    @Override
    public ProfilePageDTO getUserProfileList(User user, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? 0L : cursor;
        Pageable pageable = PageRequest.of(0, take);

        Slice <UserProfile> userProfileSlice = userProfileRepository.findAllByUserIdAndCursor(user.getId(), startCursor, pageable);

        List<ProfileDTO> profileDTOList = userProfileSlice.stream()
                .map(userProfile -> {
                    return ProfileDTO.of(user, userProfile);
                })
                .toList();

        return ProfilePageDTO.toProfileListDTO(profileDTOList, userProfileSlice);
    }

    @Override
    public MoimPreviewListDTO getUserProfileTargetMoimList(Long profileId, Long cursor, Integer take) {

        Long startCursor = (cursor == 1) ? 0L : cursor;
        Pageable pageable = PageRequest.of(0, take);

        Slice<UserMoim> userMoimSlice = userMoimRepository.findAllByUserProfileIdAndJoinStatus(profileId, JoinStatus.COMPLETE, startCursor, pageable);
        List<MoimPreviewDTO> moimPreviewDTOList = userMoimSlice.stream().map(userMoim -> {
                    return MoimPreviewDTO.toMoimPreviewDTO(userMoim.getMoim(), userMoim.getMoim().getImageUrl()!= null && !userMoim.getMoim().getImageUrl().isEmpty() ? userMoim.getMoim().getImageUrl() : null);
                }).toList();

        Long nextCursor = userMoimSlice.hasNext() && !userMoimSlice.getContent().isEmpty()
                ? userMoimSlice.getContent().get(userMoimSlice.getNumberOfElements() - 1).getId()
                : null;

        return MoimPreviewListDTO.toMoimPreviewListDTO(moimPreviewDTOList, nextCursor, userMoimSlice.hasNext());
    }

    @Override
    public ProfileDetailDTO getDetailProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(userId, MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        int participateMoimCnt = userMoimRepository.countByUserIdAndJoinStatus(userId, JoinStatus.COMPLETE);

        return ProfileDetailDTO.from(user, userProfile, participateMoimCnt);
    }

    @Override
    public UserDailyPlanPageDTO getUserDailyMoimPlans(User user, int year, int month, int day, int page, int size) {
        LocalDateTime startOfDay = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Pageable pageable = PageRequest.of(page-1, size);
        Slice<Plan> userMoimPlanSlice = planRepository.findByUserAndDateBetween(user, startOfDay, endOfDay, pageable);
        List<UserPlanDTO> userPlanDTOList = userMoimPlanSlice.map(plan -> {
            UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, plan.getMoim())
                    .orElseThrow(() -> new MoimException(USER_MOIM_NOT_FOUND));
            return UserPlanDTO.toUserMoimPlan(plan, userMoim);
        }).toList();

        return UserDailyPlanPageDTO.toUserMoimPlan(userMoimPlanSlice, userPlanDTOList);
    }

    @Override
    public UserDailyPlanPageDTO getUserDailyIndividualPlans(User user, int year, int month, int day, int page, int size) {
        LocalDateTime startOfDay = LocalDate.of(year, month, day).atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.ASC, "date"));
        Slice<IndividualPlan> userIndividualPlanSlice = individualPlanRepository.findByUserAndDateBetween(user, startOfDay, endOfDay, pageable);

        return UserDailyPlanPageDTO.toUserIndividualPlan(userIndividualPlanSlice);
    }

    @Override
    public UserDailyPlanPageDTO getUserDailyPlans(User user, int year, int month, int day, int page, int size) {
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
    public PlanMonthListDTO<List<UserPlanDTO>> getUserPlans(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<UserPlan> userPlanList = userPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate);

        Map<Integer, List<UserPlanDTO>> planListByDay = userPlanList.stream()
                .map(userPlan -> {
                    UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, userPlan.getPlan().getMoim())
                            .orElseThrow(() -> new MoimException(USER_MOIM_NOT_FOUND));
                    return UserPlanDTO.toUserMoimPlan(userPlan.getPlan(), userMoim);
                }).collect(Collectors.groupingBy(dto -> dto.time().getDayOfMonth()));

        return new PlanMonthListDTO<>(planListByDay);
    }

    @Override
    public PlanMonthListDTO<List<UserPlanDTO>> getUserMonthlyPlans(User user, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<UserPlanDTO> userMoimPlanDTOList = userPlanRepository.findByUserIdAndPlanDateBetween(user.getId(), startDate, endDate).stream()
                .map(userPlan -> {
                    UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, userPlan.getPlan().getMoim())
                            .orElseThrow(() -> new MoimException(USER_MOIM_NOT_FOUND));
                    return UserPlanDTO.toUserMoimPlan(userPlan.getPlan(), userMoim);
                }).toList();

        List<UserPlanDTO> individualPlanDTOList = individualPlanRepository.findByUserIdAndDateBetween(user.getId(), startDate, endDate).stream()
                .map(UserPlanDTO::toIndividualPlan)
                .toList();

        List<UserPlanDTO> userMoimTodoDTOList = userTodoRepository.findByUserIdAndTodoDueDateBetween(user.getId(), startDate, endDate).stream()
                .map(userTodo -> UserPlanDTO.toUserMoimTodo(userTodo.getTodo()))
                .toList();

        List<UserPlanDTO> userMonthlyPlanDTOList = Stream.concat(
                Stream.concat(userMoimPlanDTOList.stream(), individualPlanDTOList.stream()),
                userMoimTodoDTOList.stream()
        ).toList();

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
        int todoPlanCnt = userTodoRepository.countByUserAndTodoDueDateBetween(user, startOfDay, endOfDay);

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        return new UserDailyPlanCntDTO(userProfile.getName(), individualPlanCnt + moimPlanCnt + todoPlanCnt);
    }

    @Override
    public UserPlanDTO getIndividualPlanDetail(User user, Long individualPlanId) {
        IndividualPlan individualPlan = individualPlanRepository.findById(individualPlanId)
                .orElseThrow(() -> new PlanException(INDIVIDUAL_PLAN_NOT_FOUND));

        return UserPlanDTO.toIndividualPlan(individualPlan);
    }

    @Override
    public UserPlanDTO getUserMoimPlanDetail(User user, Long userMoimPlanId) {
        UserPlan userPlan = userPlanRepository.findByUserIdAndPlanId(user.getId(), userMoimPlanId)
                .orElseThrow(() -> new PlanException(PLAN_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, userPlan.getPlan().getMoim())
                .orElseThrow(() -> new MoimException(USER_MOIM_NOT_FOUND));

        return UserPlanDTO.toUserMoimPlan(userPlan.getPlan(), userMoim);
    }

    @Override
    public List<Long> findUserMoimIdListByUserId(Long userId) {
        return userMoimRepository.findByUserId(userId).stream()
                .map(userMoim -> userMoim.getMoim().getId())
                .toList();
    }

    @Override
    public Optional<IndividualPlan> findUserByPlanId(Long individualPlanId) {
        return individualPlanRepository.findById(individualPlanId);
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
    @Transactional
    public AlarmResponseListDTO getAlarms(User user, Long cursor, Integer take) {
        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        userRepository.updateLastReadTime(user, LocalDateTime.now());

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

        Set<Long> readUsersId = userRepository.findReadUserId(post);

        List<UserProfileDTO> readUsers = userRepository.findReadUsersProfileByUsersId(readUsersId, moim);

        List<UserPreviewDTO> userPreviewDTOList = readUsers.stream().map(UserPreviewDTO::toUserPreviewDTO).toList();

        return userPreviewDTOList;
    }

    @Override
    public boolean existsByProviderAndProviderId(Provider provider, String providerId) {
        return userRepository.existsByProviderAndProviderId(provider, providerId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Integer countAlarm(User user) {
        List<Alarm> alarmByUser = userRepository.findAlarmByUser(user);
        return alarmByUser.size();
    }

    @Override
    public Optional<UserProfile> findUserProfile(Long profileId) {
        return userProfileRepository.findById(profileId);
    }

    @Override
    public boolean existsByUserProfileIdAndJoinStatus(Long profileId) {
        return userMoimRepository.existsByUserProfileIdAndJoinStatus(profileId, JoinStatus.COMPLETE);
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
