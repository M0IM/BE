package com.dev.moim.global.security.feign.decoder;

import com.dev.moim.global.error.handler.AuthException;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.dev.moim.global.common.code.status.ErrorStatus.OAUTH_INVALID_TOKEN;
import static com.dev.moim.global.common.code.status.ErrorStatus.USER_PROPERTY_NOT_FOUND;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 400) {
            return new AuthException(USER_PROPERTY_NOT_FOUND);
        } else if (response.status() == 401) {
            return new AuthException(OAUTH_INVALID_TOKEN);
        }
        return FeignException.errorStatus(methodKey, response);
    }
}
