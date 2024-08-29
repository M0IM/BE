package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.Alarm;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;

import java.time.LocalDateTime;

public record AlarmResponseDTO(
        Long alarmId,
        String title,
        String content,
        AlarmDetailType alarmDetailType,
        Long targetId,
        LocalDateTime createdAt
) {
    public static AlarmResponseDTO toAlarmResponseDTO(Alarm alarm) {
        return new AlarmResponseDTO(alarm.getId(), alarm.getTitle(), alarm.getContent(), alarm.getAlarmDetailType(), alarm.getTargetId(), alarm.getCreatedAt());
    }
}
