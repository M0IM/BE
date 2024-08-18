package com.dev.moim.domain.user.dto;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.account.entity.UserProfile;

public record ChatRoomUserResponse(
        String username,
        String userProfile
) {
}
