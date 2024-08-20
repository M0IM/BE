package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.calender.PlanMonthListDTO;
import com.dev.moim.domain.moim.dto.calender.UserDailyPlanPageDTO;
import com.dev.moim.domain.moim.dto.calender.UserPlanDTO;
import com.dev.moim.domain.user.dto.ChatRoomUserListResponse;
import com.dev.moim.domain.user.dto.ProfileDTO;
import com.dev.moim.domain.user.dto.ProfileDetailDTO;
import com.dev.moim.domain.user.dto.ReviewListDTO;

import java.util.List;

public interface UserQueryService {

    ProfileDTO getProfile(User user);

    ProfileDetailDTO getDetailProfile(Long userId);

    ReviewListDTO getUserReviews(Long userId, int page, int size);

    UserDailyPlanPageDTO getUserDailyMoimPlan(User user, int year, int month, int day, int page, int size);

    UserDailyPlanPageDTO getUserDailyIndividualPlan(User user, int year, int month, int day, int page, int size);

    PlanMonthListDTO<List<UserPlanDTO>> getIndividualPlans(User user, int year, int month);

    List<Long> findUserMoimIdListByUserId(Long userId);

    Long findUserByPlanId(Long individualPlanId);

    boolean isExistEmail(String email);

//    ChatRoomUserListResponse getUserByChatRoom(User user, Long chatRoomId);
}
