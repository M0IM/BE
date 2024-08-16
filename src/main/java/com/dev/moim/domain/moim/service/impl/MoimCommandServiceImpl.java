package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.account.repository.UserProfileRepository;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.domain.moim.dto.*;
import com.dev.moim.domain.moim.entity.ExitReason;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.MoimImage;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.entity.enums.ProfileStatus;
import com.dev.moim.domain.moim.repository.ExitReasonRepository;
import com.dev.moim.domain.moim.repository.MoimImageRepository;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.UserException;
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

    @Override
    public Moim createMoim(User user, CreateMoimDTO createMoimDTO) {
        Moim moim = Moim.builder()
                .name(createMoimDTO.title())
                .introduction(createMoimDTO.introduction())
                .location(createMoimDTO.location())
                .moimCategory(createMoimDTO.moimCategory())
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
                .build();

        userMoimRepository.save(userMoim);

        createMoimDTO.imageKeyName().forEach((image)->{
            MoimImage moimImage = MoimImage.builder()
                    .imageKeyName(image)
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
                .imageKeyName(image)
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
                            .build();

        userMoimRepository.save(userMoim);
    }

    @Override
    public void acceptMoim(User user, Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(() -> new MoimException(ErrorStatus.MOIM_NOT_FOUND));
        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(() -> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        userMoim.accept();
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
}
