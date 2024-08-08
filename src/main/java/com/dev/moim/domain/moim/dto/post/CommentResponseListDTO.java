package com.dev.moim.domain.moim.dto.post;

import java.util.List;

public record CommentResponseListDTO(
        List<CommentResponseDTO> moimPreviewList,
        Long nextCursor,
        Boolean hasNext
) {
    public static CommentResponseListDTO toCommentResponseListDTO(List<CommentResponseDTO> commentLists, Long nextCursor, Boolean hasNext) {
        return new CommentResponseListDTO(commentLists, nextCursor, hasNext);
    }
}
