package com.dev.moim.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${spring.firebase.config}")
    private String firebaseConfig;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        ByteArrayInputStream serviceAccountStream = new ByteArrayInputStream(firebaseConfig.getBytes());

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .build();

        // FirebaseApp 인스턴스를 초기화하고 반환
        return FirebaseApp.initializeApp(options);
    }
}
