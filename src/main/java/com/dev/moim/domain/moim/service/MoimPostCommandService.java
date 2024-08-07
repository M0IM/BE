package com.dev.moim.domain.moim.service;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.moim.dto.CreateMoimPostDTO;
import com.dev.moim.domain.moim.entity.Post;

public interface MoimPostCommandService {
    Post createMoimPost(User user, CreateMoimPostDTO createMoimPostDTO);
}
