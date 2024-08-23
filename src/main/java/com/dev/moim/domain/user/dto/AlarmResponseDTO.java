package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.Alarm;

import java.time.LocalDateTime;

public record AlarmResponseDTO(
        Long alarmId,
        String title,
        String content,
        LocalDateTime createdAt
) {
    public static AlarmResponseDTO toAlarmResponseDTO(Alarm alarm) {
        return new AlarmResponseDTO(alarm.getId(), alarm.getTitle(), alarm.getContent(), alarm.getCreatedAt());
    }
}
