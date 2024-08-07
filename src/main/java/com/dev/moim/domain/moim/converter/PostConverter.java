package com.dev.moim.domain.moim.converter;

import com.dev.moim.domain.moim.dto.MoimPostDetailDTO;
import com.dev.moim.domain.moim.dto.MoimPostPreviewDTO;
import com.dev.moim.domain.moim.dto.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;

import java.util.List;

@RequiredArgsConstructor
public class PostConverter {

    public static MoimPostPreviewListDTO toMoimPostPreviewListDTO(Slice<Post> postList, Long nextCursor) {


        List<MoimPostPreviewDTO> posts = postList.stream().map(PostConverter::toMoimPostPreviewDTO).toList();

        return MoimPostPreviewListDTO.toMoimPostPreviewListDTO(posts, nextCursor, postList.hasNext());
    }

    public static MoimPostPreviewDTO toMoimPostPreviewDTO(Post post) {
        return MoimPostPreviewDTO.toMoimPostPreviewDTO(post);
    }
}
