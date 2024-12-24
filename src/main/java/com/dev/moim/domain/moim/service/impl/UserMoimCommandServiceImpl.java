package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.moim.dto.profile.UpdateUserMoimProfileDTO;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.service.UserMoimCommandService;
import com.dev.moim.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserMoimCommandServiceImpl implements UserMoimCommandService {

    private final S3Service s3Service;

    @Override
    public  Long updateUserMoimProfile(UserMoim userMoim, UpdateUserMoimProfileDTO request) {

        userMoim.updateProfile(
                request.nickname(),
                request.imageKey() != null && !request.imageKey().isEmpty()? s3Service.generateStaticUrl(request.imageKey()) : null,
                request.introduction(),
                request.genderVisibility(),
                request.residenceVisibility(),
                request.birthVisibility());

        return userMoim.getId();
    }
}
