package com.dev.moim.domain.account.entity.enums;

import com.dev.moim.global.error.GeneralException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_GENDER;

public enum Gender {
    MALE, FEMALE;

    @JsonCreator
    public static Gender from(String gender) {
        if (gender == null || gender.isEmpty()) {
            return null;
        }

        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (GeneralException e) {
            throw new GeneralException(INVALID_GENDER);
        }
    }
}
