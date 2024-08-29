package com.dev.moim.global.error.handler;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.GeneralException;

public class TodoException extends GeneralException {
    public TodoException(ErrorStatus errorStatus) {super(errorStatus);}
}
