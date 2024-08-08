package com.dev.moim.global.error.handler;

import com.dev.moim.global.common.code.BaseErrorCode;
import com.dev.moim.global.error.GeneralException;

public class PostException extends GeneralException {
    public PostException(BaseErrorCode code) {
        super(code);
    }
}
