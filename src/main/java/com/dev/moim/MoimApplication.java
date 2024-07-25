package com.dev.moim;

import com.dev.moim.global.security.feign.config.OauthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableConfigurationProperties(OauthProperties.class)
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class MoimApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoimApplication.class, args);
	}

}
