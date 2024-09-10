package com.dev.moim.global.firebase.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.user.dto.EventDTO;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.error.ExceptionAdvice;
import com.dev.moim.global.error.feign.dto.DiscordMessage;
import com.dev.moim.global.error.feign.service.DiscordClient;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final DiscordClient discordClient;
    private final UserQueryService userQueryService;
    private final AlarmService alarmService;
    private final UserCommandService userCommandService;
    private final Environment environment;

    public void sendEventAlarm(User owner, EventDTO eventDTO) {
        List<User> users = userQueryService.findAllUser();
        users.forEach(user -> {
            if (user.getIsEventAlarm()) {
                alarmService.saveAlarm(owner, user, eventDTO.title(), eventDTO.content(), AlarmType.EVENT, AlarmDetailType.EVENT, null, null, null);
                sendPushNotification(user, eventDTO.title(), eventDTO.content(), AlarmDetailType.EVENT);
            }
        });
    }

    public void sendPushNotification(User receiver, String title, String body, AlarmDetailType alarmDetailType) {

        if (!(receiver.getDeviceId() == null)) {

            Integer count = userQueryService.countAlarm(receiver);

            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            AndroidNotification androidNotification = AndroidNotification.builder()
                    .setChannelId(alarmDetailType.toString())
                    .build();

            AndroidConfig androidConfig = AndroidConfig.builder()
                    .setNotification(androidNotification)
                    .build();

            ApnsFcmOptions apnsFcmOptions = ApnsFcmOptions.builder()
                    .setAnalyticsLabel(alarmDetailType.toString())
                    .build();

            ApnsConfig apnsConfig = ApnsConfig.builder()
                    .setAps(Aps.builder()
                            .setAlert(ApsAlert.builder()
                                    .setTitle(title)
                                    .setBody(body)
                                    .build())
                            .setContentAvailable(true)
                            .build())
                    .setFcmOptions(apnsFcmOptions)
                    .build();

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(receiver.getDeviceId())
                    .setAndroidConfig(androidConfig)
                    .setApnsConfig(apnsConfig)
                    .putData("count", count.toString())
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                userCommandService.notDeadLockFcmSignOut(receiver);
                if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
                    discordClient.sendAlarm(createMessage(receiver, title, e));
                }
            }
        }
    }

    private DiscordMessage createMessage(User receiver, String title, Exception e) {
        return DiscordMessage.builder()
                .content("# 🚨 에러 발생 비이이이이사아아아앙")
                .embeds(
                        List.of(
                                DiscordMessage.Embed.builder()
                                        .title("ℹ️ 에러 정보")
                                        .description(String.format("%d가 유효하지 않은 fcm token값을 가지고 있습니다. \n 이유: %s\n", receiver.getId(), getStackTrace(e).substring(0, 1000)))
                                        .build()))
                .build();
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
