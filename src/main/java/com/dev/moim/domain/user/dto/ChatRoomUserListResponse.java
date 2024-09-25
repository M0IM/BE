package com.dev.moim.domain.user.dto;

import java.util.List;

public record ChatRoomUserListResponse(
        List<ChatRoomUserResponse> chatRoomUserResponses
) {
}
