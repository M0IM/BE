package com.dev.moim.domain.account.service;

import com.dev.moim.domain.account.entity.Alarm;
import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlarmCommandServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;

    @Override
    public void saveAlarm(User sender, User receiver, String title, String content, AlarmType type, AlarmDetailType alarmDetailType, Long moimId, Long postId, Long commentId) {

        Alarm alarm = Alarm.builder()
                .user(receiver)
                .content(content)
                .title(title)
                .writerId(sender.getId())
                .alarmDetailType(alarmDetailType)
                .moimId(moimId)
                .postId(postId)
                .commentId(commentId)
                .alarmType(type)
                .build();

        alarmRepository.save(alarm);
    }
}
