package com.dev.moim.global.error.handler;

import com.dev.moim.global.common.code.BaseErrorCode;
import com.dev.moim.global.error.GeneralException;

public class MoimException extends GeneralException {
    public MoimException(BaseErrorCode code) {
        super(code);
    }
}
