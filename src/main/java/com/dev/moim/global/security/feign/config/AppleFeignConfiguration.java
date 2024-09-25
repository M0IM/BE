package com.dev.moim.global.security.feign.config;

import com.dev.moim.global.security.feign.decoder.FeignErrorDecoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppleFeignConfiguration {
    @Bean(name = "appleRequestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/x-www-form-urlencoded");
    }

    @Bean(name = "appleErrorDecoder")
    public ErrorDecoder errorDecoder() {return new FeignErrorDecoder();}

    @Bean(name = "appleFeignLoggerLevel")
    Logger.Level feignLoggerLevel() {return Logger.Level.FULL;}
}
