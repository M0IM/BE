package com.dev.moim.domain.moim.dto;

import java.time.LocalDate;

public record MoimCalendarDTO(
        Long moimCalendarId,
        String title,
        String cost,
        String participant,
        String address,
        LocalDate date
) {
}
