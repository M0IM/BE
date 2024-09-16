package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.enums.Gender;

import java.time.LocalDate;

public record UpdateUserDefaultInfoDTO(
        String residence,
        Gender gender,
        LocalDate birth
) {
}
