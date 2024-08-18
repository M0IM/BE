package com.dev.moim.global.firebase.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.service.AuthService;
import com.dev.moim.global.error.feign.dto.DiscordMessage;
import com.dev.moim.global.error.feign.service.DiscordClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final DiscordClient discordClient;
    private final AuthService authService;


    public void sendNotification(User receiver, String title, String body) {

        if (!(receiver.getDeviceId() == null)) {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(receiver.getDeviceId())
                    .setNotification(notification)
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
                authService.fcmSignOut(receiver);
                discordClient.sendAlarm(createMessage(receiver, title, body));
            }
        }
    }

    private DiscordMessage createMessage(User receiver, String title, String content) {
        return DiscordMessage.builder()
                .content("# 🚨 에러 발생 비이이이이사아아아앙")
                .embeds(
                        List.of(
                                DiscordMessage.Embed.builder()
                                        .title("ℹ️ 에러 정보")
                                        .description(String.format("%d가 유효하지 않은 fcm token값을 가지고 있습니다.", receiver.getId()))
                                        .build()))
                .build();
    }
}
