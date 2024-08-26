package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.user.dto.UserDailyPlanPageDTO;
import com.dev.moim.domain.user.dto.UserPlanDTO;
import com.dev.moim.domain.user.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

    ProfileDTO getProfile(User user);

    ProfileDetailDTO getDetailProfile(Long userId);

    ReviewListDTO getUserReviews(Long userId, int page, int size);

    PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month);

    PlanMonthListDTO<List<UserPlanDTO>> getUserMonthlyPlans(User user, int year, int month);

    UserDailyPlanPageDTO getUserDailyMoimPlan(User user, int year, int month, int day, int page, int size);

    UserDailyPlanPageDTO getUserDailyIndividualPlan(User user, int year, int month, int day, int page, int size);

    UserDailyPlanPageDTO getUserDailyPlanList(User user, int year, int month, int day, int page, int size);

    UserDailyPlanCntDTO getUserDailyPlanCnt(User user, int year, int month, int day);

    List<Long> findUserMoimIdListByUserId(Long userId);

    Long findUserByPlanId(Long individualPlanId);

    boolean isExistEmail(String email);

    boolean isMoimOwner(User user);

    List<User> findAllUser();

    Optional<User> findUserById(Long userId);

    AlarmResponseListDTO getAlarms(User user, Long cursor, Integer take);

    List<UserPreviewDTO> findUnReadUserListByPost(User user, Long moimId, Long postId);

//    ChatRoomUserListResponse getUserByChatRoom(User user, Long chatRoomId);
}
