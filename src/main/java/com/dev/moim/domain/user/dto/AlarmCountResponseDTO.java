package com.dev.moim.domain.user.dto;

public record AlarmCountResponseDTO(
        Integer remainAlarms
) {
    public static AlarmCountResponseDTO toAlarmCountResponseDTO(Integer remainAlarms) {
        return new AlarmCountResponseDTO(remainAlarms);
    }
}
