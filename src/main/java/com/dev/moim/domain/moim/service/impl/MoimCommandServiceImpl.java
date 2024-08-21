package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.dto.*;
import com.dev.moim.domain.moim.entity.*;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.ProfileStatus;
import com.dev.moim.domain.moim.repository.*;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.firebase.service.FcmService;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MoimCommandServiceImpl implements MoimCommandService {

    private final MoimRepository moimRepository;
    private final MoimImageRepository moimImageRepository;
    private final UserMoimRepository userMoimRepository;
    private final ExitReasonRepository exitReasonRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final FcmService fcmService;

    @Override
    public Moim createMoim(User user, CreateMoimDTO createMoimDTO) {
        Moim moim = Moim.builder()
                .name(createMoimDTO.title())
                .introduction(createMoimDTO.introduction())
                .location(createMoimDTO.location())
                .moimCategory(createMoimDTO.moimCategory())
                .introduceVideoKeyName(s3Service.generateStaticUrl(createMoimDTO.introduceVideoKeyName()))
                .introduceVideoTitle(createMoimDTO.introduceVideoTitle())
                .build();

        moimRepository.save(moim);

        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN).orElseThrow(()-> new MoimException(ErrorStatus.USER_PROFILE_NOT_FOUND_MAIN));


        UserMoim userMoim = UserMoim.builder()
                .moim(moim)
                .user(user)
                .moimRole(MoimRole.OWNER)
                .joinStatus(JoinStatus.COMPLETE)
                .profileStatus(ProfileStatus.PRIVATE)
                .userProfile(userProfile)
                .confirm(true)
                .build();

        userMoimRepository.save(userMoim);

        createMoimDTO.imageKeyName().forEach((image)->{
            MoimImage moimImage = MoimImage.builder()
                    .imageKeyName(s3Service.generateStaticUrl(image))
                    .moim(moim)
                    .build();

            moimImageRepository.save(moimImage);
        });

        return moim;
    }

    @Override
    public void withDrawMoim(User user, WithMoimDTO withMoimDTO) {
        Moim moim = moimRepository.findById(withMoimDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        ExitReason exitReason = ExitReason.builder().user(user).moim(moim).content(withMoimDTO.exitReason()).build();

        exitReasonRepository.save(exitReason);

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoimRepository.delete(userMoim);
    }

    @Override
    public void modifyMoimInfo(UpdateMoimDTO updateMoimDTO) {
        Moim moim = moimRepository.findById(updateMoimDTO.moimId()).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        List<MoimImage> moimImageList = updateMoimDTO.imageKeyNames().stream().map((image)-> MoimImage.builder()
                .imageKeyName(s3Service.generateStaticUrl(image))
                .moim(moim)
                .build()
        ).toList();

        moim.updateMoim(moim.getName(), moim.getIntroduction(), moim.getIntroduction(), moimImageList);
    }

    @Override
    public void joinMoim(User user, Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserProfile userProfile = userProfileRepository.findByUserIdAndProfileType(user.getId(), ProfileType.MAIN).orElseThrow(()-> new MoimException(ErrorStatus.USER_PROFILE_NOT_FOUND_MAIN));
        UserMoim userMoim = UserMoim.builder()
                            .userProfile(userProfile)
                            .joinStatus(JoinStatus.LOADING)
                            .user(user)
                            .moimRole(MoimRole.MEMBER)
                            .moim(moim)
                            .profileStatus(ProfileStatus.PRIVATE)
                            .confirm(false)
                            .build();

        Boolean isRequest = userMoimRepository.findByUserAndMoimAndJoinRequest(user, moim, List.of(JoinStatus.LOADING, JoinStatus.COMPLETE));

        if (isRequest) {
            throw new MoimException(ErrorStatus.ALREADY_REQUEST);
        }

        userMoimRepository.save(userMoim);
    }

    @Override
    public void acceptMoim(MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO) {
        User user = userRepository.findById(moimJoinConfirmRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));


        Moim moim = moimRepository.findById(moimJoinConfirmRequestDTO.moimId()).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.LOADING).orElseThrow(() -> new MoimException(ErrorStatus.NOT_REQUEST_JOIN));

        userMoim.accept();

        if (user.getIsPushAlarm()) {
            fcmService.sendNotification(user,  moim.getName() + " 모임에 가입되었습니다", moim.getName() + "에 가입되었습니다");
        }
    }

    @Override
    public ChangeAuthorityResponseDTO changeMemberAuthorities(User user, ChangeAuthorityRequestDTO changeAuthorityRequestDTO) {
        User targetUser = userRepository.findById(changeAuthorityRequestDTO.userId()).orElseThrow(() -> {
            throw new UserException(ErrorStatus.USER_NOT_FOUND);
        });
        Moim moim = moimRepository.findById(changeAuthorityRequestDTO.moimId()).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserAndMoim(targetUser, moim).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoim.changeStatus(changeAuthorityRequestDTO.moimRole());

        return new ChangeAuthorityResponseDTO(targetUser.getId(), userMoim.getMoimRole());
    }

    @Override
    public void rejectMoims(MoimJoinConfirmRequestDTO moimJoinConfirmRequestDTO) {
        User user = userRepository.findById(moimJoinConfirmRequestDTO.userId()).orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        Moim moim = moimRepository.findById(moimJoinConfirmRequestDTO.moimId()).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserIdAndMoimId(user.getId(), moim.getId(), JoinStatus.LOADING).orElseThrow(() -> new MoimException(ErrorStatus.NOT_REQUEST_JOIN));

        userMoim.reject();

        if (user.getIsPushAlarm()) {
            fcmService.sendNotification(user, moim.getName()  + " 의 모임에 반려되셨습니다", moim.getName() + "에게 반려 되셨습니다.");
        }
    }
}
