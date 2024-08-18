package com.dev.moim.domain.account.service;


import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmType;

public interface AlarmService {
    void saveAlarm(User sender, User receiver, String title, String content, AlarmType alarmType);

}
