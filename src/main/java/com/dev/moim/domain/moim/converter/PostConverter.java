package com.dev.moim.domain.moim.converter;

import com.dev.moim.domain.moim.dto.post.CommentResponseDTO;
import com.dev.moim.domain.moim.dto.post.CommentResponseListDTO;
import com.dev.moim.domain.moim.dto.post.MoimPostPreviewDTO;
import com.dev.moim.domain.moim.dto.post.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Comment;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@RequiredArgsConstructor
public class PostConverter {

    private final PostQueryService postQueryService;

    public static MoimPostPreviewListDTO toMoimPostPreviewListDTO(Slice<Post> postList, Long nextCursor) {


        List<MoimPostPreviewDTO> posts = postList.stream().map(PostConverter::toMoimPostPreviewDTO).toList();

        return MoimPostPreviewListDTO.toMoimPostPreviewListDTO(posts, nextCursor, postList.hasNext());
    }

    public static MoimPostPreviewDTO toMoimPostPreviewDTO(Post post) {
        return MoimPostPreviewDTO.toMoimPostPreviewDTO(post);
    }
}
