package com.dev.moim.domain.user.dto;

import java.util.List;

public record AlarmResponseListDTO(
        Long nextCursor,
        Boolean hasNext,
        List<AlarmResponseDTO> alarmResponseDTOList
) {
    public static AlarmResponseListDTO toAlarmResponseListDTO(List<AlarmResponseDTO> alarmResponseDTOList, Long cursor, Boolean hasNext) {
        return new AlarmResponseListDTO(cursor, hasNext, alarmResponseDTOList);
    }
}
