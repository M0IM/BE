package com.dev.moim.global.security.feign.config;

import com.dev.moim.global.error.decoder.FeignErrorDecoder;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleFeignConfiguration {
    @Bean(name = "googleRequestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return template -> template.header("Content-Type", "application/json; charset=UTF-8");
    }

    @Bean(name = "googleErrorDecoder")
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean(name = "googleFeignLoggerLevel")
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}