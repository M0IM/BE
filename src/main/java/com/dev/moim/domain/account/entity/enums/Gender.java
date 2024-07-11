package com.dev.moim.domain.account.entity.enums;

import com.dev.moim.global.error.GeneralException;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_GENDER;

public enum Gender {
    MALE, FEMALE;

    public static Gender from(String gender) {
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (GeneralException e) {
            throw new GeneralException(INVALID_GENDER);
        }
    }
}
