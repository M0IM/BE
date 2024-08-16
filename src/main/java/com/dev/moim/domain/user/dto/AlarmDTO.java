package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;

public record AlarmDTO(
        Boolean isPushAlarm,
        Boolean isEventAlarm
) {
    public static AlarmDTO toAlarmDTO(User user) {
        return new AlarmDTO(user.getIsPushAlarm(), user.getIsEventAlarm());
    }
}
