package com.dev.moim.domain.account.dto;

import com.dev.moim.domain.account.entity.enums.Provider;
import com.dev.moim.global.validation.annotation.ExistEmailValidation;
import com.dev.moim.global.validation.annotation.UpdatePasswordValidation;

public record UpdatePasswordDTO(
        Provider provider,
        @ExistEmailValidation String email,
        @UpdatePasswordValidation String password
) {
}
