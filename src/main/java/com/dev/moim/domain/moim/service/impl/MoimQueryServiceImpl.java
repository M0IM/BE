package com.dev.moim.domain.moim.service.impl;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.MoimRequestType;
import com.dev.moim.domain.moim.dto.MoimPreviewDTO;
import com.dev.moim.domain.moim.dto.MoimPreviewListDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.MoimImage;
import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.service.MoimQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MoimQueryServiceImpl implements MoimQueryService {

    private final MoimRepository moimRepository;

    @Override
    public MoimPreviewListDTO getMyMoim(User user, Long cursor, Integer take) {

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Moim> myMoims = moimRepository.findMyMoims(user, cursor, PageRequest.of(0, take));

        List<MoimPreviewDTO> findMyMoims = myMoims.stream().map((moim)->{
            List<String> imageKeys = moim.getMoimImages().stream().map((MoimImage::getImageKeyName)).toList();
            return MoimPreviewDTO.toMoimPreviewDTO(moim, imageKeys);
        }).toList();

        Long nextCursor = null;
        if (!myMoims.isLast()) {
            nextCursor = myMoims.toList().get(myMoims.toList().size() - 1).getId();
        }

        return MoimPreviewListDTO.toMoimPreviewListDTO(findMyMoims, nextCursor, myMoims.hasNext());
    }

    @Override
    public MoimPreviewListDTO findMoims(MoimRequestType moimRequestType, String name, Long cursor, Integer take) {

        if (cursor == 1) {
            cursor = Long.MAX_VALUE;
        }

        Slice<Moim> moimSlice;
        if (moimRequestType.equals(MoimRequestType.ALL)) {
            moimSlice = moimRepository.findByNameLikeAndIdLessThanOrderByIdDesc("%"+name+"%", cursor, PageRequest.of(0, take));
        } else {
            MoimCategory moimCategory = MoimCategory.valueOf(moimRequestType.toString());
            moimSlice = moimRepository.findByMoimCategoryAndNameLikeAndIdLessThanOrderByIdDesc(moimCategory, "%"+name+"%", cursor, PageRequest.of(0, take));
        }

        Long nextCursor = null;
        if (!moimSlice.isLast()) {
            nextCursor = moimSlice.toList().get(moimSlice.toList().size() - 1).getId();
        }

        List<MoimPreviewDTO> moimPreviewDTOList = moimSlice.toList().stream().map((moim) -> {
            List<String> imageKeys = moim.getMoimImages().stream().map((MoimImage::getImageKeyName)).toList();
            return MoimPreviewDTO.toMoimPreviewDTO(moim, imageKeys);
        }).toList();

        return MoimPreviewListDTO.toMoimPreviewListDTO(moimPreviewDTOList, nextCursor, moimSlice.hasNext());
    }
}
