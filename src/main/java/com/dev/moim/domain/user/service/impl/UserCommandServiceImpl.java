package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.AlarmRepository;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.moim.repository.IndividualPlanRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.user.dto.*;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.global.error.handler.IndividualPlanException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.dev.moim.domain.moim.entity.enums.ProfileStatus.PRIVATE;
import static com.dev.moim.domain.moim.entity.enums.ProfileStatus.PUBLIC;
import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserProfileRepository userProfileRepository;
    private final UserMoimRepository userMoimRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final IndividualPlanRepository individualPlanRepository;
    private final AlarmRepository alarmRepository;

    @Override
    public void createProfile(User user, CreateProfileDTO request) {

        if (request.profileType() == ProfileType.MAIN) {
            UserProfile mainProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN)
                    .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND_MAIN));

            mainProfile.updateProfileType(ProfileType.SUB);
        }

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .name(request.nickname())
                .residence(request.residence())
                .introduction(request.introduction())
                .imageUrl(request.imageKey() != null && !request.imageKey().isEmpty() ? s3Service.generateStaticUrl(request.imageKey()) : null)
                .profileType(request.profileType())
                .build();

        userProfileRepository.save(userProfile);

        userMoimRepository.findAllByUserIdAndMoimIdList(user.getId(), request.targetMoimIdList())
                .forEach(userMoim -> userMoim.updateUserProfile(userProfile));
    }

    @Override
    public void updateInfo(User user, UpdateUserInfoDTO request) {

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        userProfile.updateUser(
                request.nickname(),
                request.residence(),
                request.introduction(),
                request.imageKey() != null && !request.imageKey().isEmpty()? s3Service.generateStaticUrl(request.imageKey()) : null
        );

        userMoimRepository.findByUserId(user.getId()).forEach(userMoim ->
                userMoim.updateProfileStatus(request.publicMoimList().contains(userMoim.getMoim().getId()) ? PUBLIC : PRIVATE)
        );
    }

    @Override
    public AlarmDTO settingPushAlarm(User user) {
        user.changePushAlarm();
        return AlarmDTO.toAlarmDTO(user);
    }

    @Override
    public AlarmDTO settingEventAlarm(User user) {
        user.changeEventAlarm();
        return AlarmDTO.toAlarmDTO(user);
    }

    @Override
    public void createIndividualPlan(User user, CreateIndividualPlanRequestDTO request) {
        IndividualPlan individualPlan = IndividualPlan.builder()
                .title(request.title())
                .location(request.location())
                .locationDetail(request.locationDetail())
                .memo(request.memo())
                .date(LocalDateTime.of(request.date(), request.startTime()))
                .user(user)
                .build();

        individualPlanRepository.save(individualPlan);
    }

    @Override
    public void deleteIndividualPlan(Long individualPlanId) {
        individualPlanRepository.deleteById(individualPlanId);
    }

    @Override
    public void updateIndividualPlan(Long individualPlanId, CreateIndividualPlanRequestDTO request) {
        IndividualPlan individualPlan = individualPlanRepository.findById(individualPlanId)
                .orElseThrow(() -> new IndividualPlanException(INDIVIDUAL_PLAN_NOT_FOUND));

        individualPlan.updateIndividualPlan(
                request.title(),
                LocalDateTime.of(request.date(), request.startTime()),
                request.location(),
                request.locationDetail(),
                request.memo()
        );
    }

    @Override
    public void fcmSignOut(User user) {
        user.fcmSignOut();
        userRepository.save(user);
    }

    @Override
    public void notDeadLockFcmSignOut(User user) {
        userRepository.updateFcmTokenByUser(user);
    }

    @Override
    public void deleteAlarms(User user) {
        alarmRepository.deleteAllByUser(user);
    }
}
