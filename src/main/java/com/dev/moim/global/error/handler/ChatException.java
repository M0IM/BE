package com.dev.moim.global.error.handler;


import com.dev.moim.global.common.code.BaseErrorCode;
import com.dev.moim.global.error.GeneralException;

public class ChatException extends GeneralException {
    public ChatException(BaseErrorCode code) {
        super(code);
    }
}
