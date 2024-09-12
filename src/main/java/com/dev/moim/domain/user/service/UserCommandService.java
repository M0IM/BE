package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.*;

public interface UserCommandService {

    void createProfile(User user, CreateProfileDTO request);

    void updateUserProfile(User user, Long profileId, UpdateMultiProfileDTO request);

    void deleteUserProfile(Long profileId);

    void updateUserDefaultInfo(User user, UpdateUserInfoDTO request);

    AlarmDTO settingPushAlarm(User user);

    AlarmDTO settingEventAlarm(User user);

    void createIndividualPlan(User user, CreateIndividualPlanRequestDTO request);

    void deleteIndividualPlan(Long individualPlanId);

    void updateIndividualPlan(Long individualPlanId, CreateIndividualPlanRequestDTO request);

    void fcmSignOut(User user);

    void notDeadLockFcmSignOut(User user);

    void deleteAlarms(User user);
}
