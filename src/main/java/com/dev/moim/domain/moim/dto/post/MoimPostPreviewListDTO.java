package com.dev.moim.domain.moim.dto.post;

import com.dev.moim.domain.moim.entity.Post;
import org.springframework.data.domain.Slice;

import java.util.List;

public record MoimPostPreviewListDTO(
        List<MoimPostPreviewDTO> moimPreviewList,
        Long nextCursor,
        Boolean hasNext
) {
    public static MoimPostPreviewListDTO toMoimPostPreviewListDTO(List<MoimPostPreviewDTO> moimPreviewList, Long nextCursor, Boolean hasNext) {
        return new MoimPostPreviewListDTO(moimPreviewList, nextCursor, hasNext);
    }

    public static MoimPostPreviewListDTO from(Slice<Post> slice, Long nextCursor) {
        List<MoimPostPreviewDTO> list = slice.stream()
                .map(MoimPostPreviewDTO::toMoimPostPreviewDTO)
                .toList();

        return new MoimPostPreviewListDTO(
                list,
                nextCursor,
                slice.hasNext());
    }
}
