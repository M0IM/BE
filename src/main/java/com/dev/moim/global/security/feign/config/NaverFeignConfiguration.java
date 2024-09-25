package com.dev.moim.global.security.feign.config;

import com.dev.moim.global.security.feign.decoder.FeignErrorDecoder;
import feign.Logger;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NaverFeignConfiguration {

    @Bean(name = "naverErrorDecoder")
    public ErrorDecoder errorDecoder() {return new FeignErrorDecoder();}

    @Bean(name = "naverFeignLoggerLevel")
    Logger.Level feignLoggerLevel() {return Logger.Level.FULL;}
}
