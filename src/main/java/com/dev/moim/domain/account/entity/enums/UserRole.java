package com.dev.moim.domain.account.entity.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {
    ROLE_USER("유저"),
    ROLE_ADMIN("관리자");

    private final String description;
}
