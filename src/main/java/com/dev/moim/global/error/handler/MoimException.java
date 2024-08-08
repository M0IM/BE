package com.dev.moim.global.error.handler;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.GeneralException;

public class MoimException extends GeneralException {
    public MoimException(ErrorStatus errorStatus) {super(errorStatus);}
}
