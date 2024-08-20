package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;

import java.util.List;

public record CreateMoimDTO(
        String title,
        String location,
        MoimCategory moimCategory,
        String imageKeyName,
        String introduceVideoKeyName,
        String introduceVideoTitle,
        String introduction
) {

}
