package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UpdateUserDefaultInfoDTO(
        @NotBlank String residence,
        Gender gender,
        LocalDate birth
) {
}
