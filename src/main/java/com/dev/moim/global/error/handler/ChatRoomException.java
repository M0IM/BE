package com.dev.moim.global.error.handler;


import com.dev.moim.global.common.code.BaseErrorCode;
import com.dev.moim.global.error.GeneralException;

public class ChatRoomException extends GeneralException {
    public ChatRoomException(BaseErrorCode code) {
        super(code);
    }
}
