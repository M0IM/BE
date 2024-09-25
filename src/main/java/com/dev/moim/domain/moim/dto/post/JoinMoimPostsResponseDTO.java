package com.dev.moim.domain.moim.dto.post;

import java.util.List;

public record JoinMoimPostsResponseDTO(
        Long moimId,
        String moimTitle,
        List<MoimPostPreviewDTO> moimPostPreviewDTOList
) {
    public static JoinMoimPostsResponseDTO toJoinMoimPostsResponseDTO(Long moimdId, String moimTitle, List<MoimPostPreviewDTO> moimPostPreviewDTOList) {
        return new JoinMoimPostsResponseDTO(moimdId, moimTitle, moimPostPreviewDTOList);
    }

}
