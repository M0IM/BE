package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.controller.enums.PostRequestType;
import com.dev.moim.domain.moim.dto.MoimPostPreviewListDTO;
import com.dev.moim.domain.moim.entity.Post;
import com.dev.moim.domain.moim.entity.enums.PostType;

public interface MoimPostQueryService {
    MoimPostPreviewListDTO getMoimPostList(User user, Long moimId, PostRequestType postRequestType, Long cursor, Integer take);

    Post getMoimPost(User user, Long moimId, Long postId);
}
