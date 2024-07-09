package com.dev.moim.domain.account.dto;

import java.time.LocalDate;

public record JoinRequest(
        String nickname,
        String email,
        String password,
        String role,
        String gender,
        int age,
        LocalDate birth,
        String residence,
        String introduction
) {
}