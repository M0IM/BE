package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.user.dto.UserDailyPlanPageDTO;
import com.dev.moim.domain.user.dto.UserPlanDTO;
import com.dev.moim.domain.user.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

    ProfileDTO getProfile(User user);

    ProfilePageDTO getUserProfileList(User user, Long cursor, Integer take);

    ProfileDetailDTO getDetailProfile(Long userId);

    PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month);

    PlanMonthListDTO<List<UserPlanDTO>> getUserPlans(User user, int year, int month);

    PlanMonthListDTO<List<UserPlanDTO>> getUserMonthlyPlans(User user, int year, int month);

    UserDailyPlanPageDTO getUserDailyMoimPlans(User user, int year, int month, int day, int page, int size);

    UserDailyPlanPageDTO getUserDailyIndividualPlans(User user, int year, int month, int day, int page, int size);

    UserDailyPlanPageDTO getUserDailyPlans(User user, int year, int month, int day, int page, int size);

    UserDailyPlanCntDTO getUserDailyPlanCnt(User user, int year, int month, int day);

    UserPlanDTO getIndividualPlanDetail(User user, Long individualPlanId);

    UserPlanDTO getUserMoimPlanDetail(User user, Long userMoimPlanId);

    List<Long> findUserMoimIdListByUserId(Long userId);

    Optional<IndividualPlan> findUserByPlanId(Long individualPlanId);

    boolean isExistEmail(String email);

    boolean isMoimOwner(User user);

    List<User> findAllUser();

    Optional<User> findUserById(Long userId);

    AlarmResponseListDTO getAlarms(User user, Long cursor, Integer take);

    List<UserPreviewDTO> findUnReadUserListByPost(User user, Long moimId, Long postId);

    boolean existsByProviderAndProviderId(Provider provider, String providerId);

    boolean existsByEmail(String email);

    Integer countAlarm(User user);

    Optional<UserProfile> findUserProfile(Long profileId);

    boolean existsByUserProfileIdAndJoinStatus(Long profileId);

//    ChatRoomUserListResponse getUserByChatRoom(User user, Long chatRoomId);
}
