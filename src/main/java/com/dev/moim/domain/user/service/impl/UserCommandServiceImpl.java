package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.UserReview;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.repository.UserReviewRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.user.dto.AlarmDTO;
import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.CreateReviewResultDTO;
import com.dev.moim.domain.user.dto.UpdateUserInfoDTO;
import com.dev.moim.domain.user.service.UserCommandService;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.dev.moim.domain.moim.entity.enums.ProfileStatus.PRIVATE;
import static com.dev.moim.domain.moim.entity.enums.ProfileStatus.PUBLIC;
import static com.dev.moim.global.common.code.status.ErrorStatus.USER_NOT_FOUND;
import static com.dev.moim.global.common.code.status.ErrorStatus.USER_PROFILE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandServiceImpl implements UserCommandService {

    private final UserProfileRepository userProfileRepository;
    private final UserMoimRepository userMoimRepository;
    private final UserRepository userRepository;
    private final UserReviewRepository userReviewRepository;
    private final S3Service s3Service;

    @Override
    public void updateInfo(User user, UpdateUserInfoDTO request) {

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN)
                .orElseThrow(() -> new UserException(USER_PROFILE_NOT_FOUND));

        Optional.ofNullable(request.imageKey())
                .map(s3Service::generateStaticUrl)
                .ifPresent(userProfile::updateImageUrl);

        userProfile.updateUser(
                request.nickname(),
                request.residence(),
                request.introduction()
        );

        userMoimRepository.findByUserId(user.getId()).forEach(userMoim ->
                userMoim.updateProfileStatus(request.publicMoimList().contains(userMoim.getMoim().getId()) ? PUBLIC : PRIVATE)
        );
    }

    @Override
    public CreateReviewResultDTO postMemberReview(User user, CreateReviewDTO request) {

        User targetUser = userRepository.findById(request.targetUserId())
                .orElseThrow(() -> new UserException(USER_NOT_FOUND));

        UserReview userReview = UserReview.builder()
                .rating(request.rating())
                .content(request.content())
                .user(targetUser)
                .writerId(user.getId())
                .build();
        userReviewRepository.save(userReview);

        double averageRating = userReviewRepository.findAllByUserId(targetUser.getId())
                .stream()
                .mapToDouble(UserReview::getRating)
                .average()
                .orElse(0.0);

        targetUser.updateRating(averageRating);

        return CreateReviewResultDTO.of(userReview);
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
}
