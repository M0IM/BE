package com.dev.moim.domain.user.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserReview;
import com.dev.moim.domain.account.entity.enums.AlarmDetailType;
import com.dev.moim.domain.account.entity.enums.AlarmType;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.account.repository.UserReviewRepository;
import com.dev.moim.domain.account.service.AlarmService;
import com.dev.moim.domain.user.dto.CreateReviewDTO;
import com.dev.moim.domain.user.dto.CreateReviewResultDTO;
import com.dev.moim.domain.user.service.ReviewCommandService;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.firebase.service.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final UserReviewRepository userReviewRepository;
    private final UserRepository userRepository;
    private final AlarmService alarmService;
    private final FcmService fcmService;

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

        if (targetUser.getIsPushAlarm()) {
            alarmService.saveAlarm(user, targetUser, "후기가 도착했습니다" , "새로 도착한 후기를 확인해주세요", AlarmType.PUSH, AlarmDetailType.REVIEW, null, null, null);
            fcmService.sendPushNotification(targetUser, "후기가 도착했습니다" , "새로 도착한 후기를 확인해주세요", AlarmDetailType.REVIEW);
        }

        return CreateReviewResultDTO.of(userReview);
    }
}
