package com.dev.moim.global.error.handler;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.GeneralException;

public class PlanException extends GeneralException {
    public PlanException(ErrorStatus errorStatus) {super(errorStatus);}
}
