package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.MoimPreviewDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.MoimImage;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.service.MoimQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimQueryServiceImpl implements MoimQueryService {

    private final MoimRepository moimRepository;

    @Override
    public MoimPreviewListDTO getMyMoim(User user) {
        List<Moim> myMoims = moimRepository.findMyMoims(user);

        List<MoimPreviewDTO> findMyMoims = myMoims.stream().map((moim)->{
            List<String> imageKeys = moim.getMoimImages().stream().map((MoimImage::getImageKeyName)).toList();
            return MoimPreviewDTO.toMoimPreviewDTO(moim, imageKeys);
        }).toList();

        return MoimPreviewListDTO.toMoimPreviewListDTO(findMyMoims);
    }
}
