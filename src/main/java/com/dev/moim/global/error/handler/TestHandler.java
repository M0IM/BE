package com.dev.moim.global.error.handler;

import com.dev.moim.global.common.code.BaseErrorCode;
import com.dev.moim.global.error.GeneralException;

public class TestHandler extends GeneralException {
    public TestHandler(BaseErrorCode code) {
        super(code);
    }
}
