package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.Alarm;

public record AlarmResponseDTO(
        Long alarmId,
        String title,
        String content
) {
    public static AlarmResponseDTO toAlarmResponseDTO(Alarm alarm) {
        return new AlarmResponseDTO(alarm.getId(), alarm.getTitle(), alarm.getContent());
    }
}
