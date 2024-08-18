package com.dev.moim.domain.user.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.dto.*;

public interface UserCommandService {

    void updateInfo(User user, UpdateUserInfoDTO request);

    CreateReviewResultDTO postMemberReview(User user, CreateReviewDTO request);

    AlarmDTO settingPushAlarm(User user);

    AlarmDTO settingEventAlarm(User user);

    void sendEventAlarm(EventDTO eventDTO);
}
