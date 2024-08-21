package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.dto.calender.UserDailyPlanPageDTO;
import com.dev.moim.domain.moim.dto.calender.UserPlanDTO;
import com.dev.moim.domain.user.dto.*;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

    ProfileDTO getProfile(User user);

    ProfileDetailDTO getDetailProfile(Long userId);

    ReviewListDTO getUserReviews(Long userId, int page, int size);

    UserDailyPlanPageDTO getUserDailyMoimPlan(User user, int year, int month, int day, int page, int size);

    UserDailyPlanPageDTO getUserDailyIndividualPlan(User user, int year, int month, int day, int page, int size);

    PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month);

    UserDailyPlanCntDTO getUserDailyPlanCnt(User user, int year, int month, int day);

    List<Long> findUserMoimIdListByUserId(Long userId);

    Long findUserByPlanId(Long individualPlanId);

    boolean isExistEmail(String email);

    boolean isMoimOwner(User user);

    List<User> findAllUser();

    Optional<User> findUserById(Long userId);

//    ChatRoomUserListResponse getUserByChatRoom(User user, Long chatRoomId);
}
