package com.dev.moim.global.security.feign.decoder;

import com.dev.moim.global.error.handler.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {

        log.error("response.status : {}", response.status());
        log.error("response.body : {}", response.body());
        log.error("response : {}", response);

        if (response.status() == 400) {
            log.error("ERROR STATUS 400");
            return new FeignException(USER_PROPERTY_NOT_FOUND);
        } else if (response.status() == 401) {
            log.error("ERROR STATUS 401");
            return new FeignException(OAUTH_INVALID_TOKEN);
        } else {
            return new FeignException(FEIGN_UNKNOWN_ERROR);
        }
    }
}
