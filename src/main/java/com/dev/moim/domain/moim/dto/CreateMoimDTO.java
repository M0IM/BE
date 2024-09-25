package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.MoimCategory;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record CreateMoimDTO(
        @Length(min = 1, max = 255)
        String title,
        @Length(min = 1, max = 255)
        String location,
        MoimCategory moimCategory,
        String imageKeyName,
        String introduceVideoKeyName,
        @Length(min = 1, max = 255)
        String introduceVideoTitle,
        @Length(min = 1, max = 255)
        String introduction
) {

}
