package com.dev.moim.domain.moim.dto;

import com.dev.moim.domain.moim.entity.enums.JoinStatus;
import com.dev.moim.domain.moim.entity.enums.MoimRole;

public record MoimRoleResponse(
        MoimRole moimRole,
        JoinStatus joinStatus
) {
    public static MoimRoleResponse toMoimRoleResponse(MoimRole moimRole, JoinStatus joinStatus) {
        return new MoimRoleResponse(moimRole, joinStatus);
    }
}
