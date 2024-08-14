package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.CreateMoimDTO;
import com.dev.moim.domain.moim.dto.CreateMoimResultDTO;
import com.dev.moim.domain.moim.dto.WithMoimDTO;
import com.dev.moim.domain.moim.entity.ExitReason;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.MoimImage;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.repository.ExitReasonRepository;
import com.dev.moim.domain.moim.repository.MoimImageRepository;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.domain.moim.service.MoimCommandService;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MoimCommandServiceImpl implements MoimCommandService {

    private final MoimRepository moimRepository;
    private final MoimImageRepository moimImageRepository;
    private final UserMoimRepository userMoimRepository;
    private final ExitReasonRepository exitReasonRepository;

    @Override
    public Moim createMoim(User user, CreateMoimDTO createMoimDTO) {
        Moim moim = Moim.builder()
                .name(createMoimDTO.title())
                .introduction(createMoimDTO.introduction())
                .location(createMoimDTO.location())
                .moimCategory(createMoimDTO.moimCategory())
                .build();

        moimRepository.save(moim);

        UserMoim userMoim = UserMoim.builder()
                .moim(moim)
                .user(user)
                .moimRole(MoimRole.ADMIN)
                .joinStatus(JoinStatus.COMPLETE)
//                .userProfile()       profile 관련 api 완성되면 추가할 예정
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
}
