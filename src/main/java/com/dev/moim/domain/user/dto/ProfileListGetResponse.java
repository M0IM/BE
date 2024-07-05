package com.dev.moim.domain.user.dto;

import java.util.List;

public record ProfileListGetResponse(
        List<ProfileResponse> profileResponseList,
        Long mainProfileId
) {
}
