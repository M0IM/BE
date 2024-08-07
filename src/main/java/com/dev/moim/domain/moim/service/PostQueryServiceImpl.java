package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.converter.PostConverter;
import com.dev.moim.domain.moim.dto.post.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Moim;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.PostType;
import com.dev.moim.domain.moim.repository.PostRepository;
import com.dev.moim.domain.moim.repository.MoimRepository;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final PostRepository postRepository;
    private final MoimRepository moimRepository;
    private final UserMoimRepository userMoimRepository;

    @Override
    public MoimPostPreviewListDTO getMoimPostList(User user, Long moimId, PostRequestType postRequestType, Long cursor, Integer take) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

        UserMoim userMoim = userMoimRepository.findByUserAndMoim(user, moim).orElseThrow(()-> new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN));

        cursor = Long.MAX_VALUE;

        Slice<Post> postSlices;
        if (postRequestType.equals(PostRequestType.ALL)) {
            postSlices = postRepository.findByUserMoimAndIdLessThanOrderByIdDesc(userMoim, cursor, PageRequest.of(0, take));
        } else {
            PostType postType = PostType.valueOf(postRequestType.toString());
            postSlices = postRepository.findByUserMoimAndPostTypeAndIdLessThanOrderByIdDesc(userMoim, postType, cursor, PageRequest.of(0, take));
        }

        Long nextCursor = null;
        if (!postSlices.isLast()) {
            nextCursor = postSlices.toList().get(postSlices.toList().size() - 1).getId();
        }

        return PostConverter.toMoimPostPreviewListDTO(postSlices, nextCursor);
    }

    @Override
    public Post getMoimPost(User user, Long moimId, Long postId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(()-> new MoimException(ErrorStatus.MOIM_NOT_FOUND));

       if(!userMoimRepository.existsByUserAndMoim(user, moim)) {
           throw new MoimException(ErrorStatus.USER_NOT_MOIM_JOIN);
       }

        return postRepository.findById(postId).orElseThrow(()-> new PostException(ErrorStatus.POST_NOT_FOUND));
    }
}
