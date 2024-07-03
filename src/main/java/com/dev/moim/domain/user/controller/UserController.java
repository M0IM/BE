package com.dev.moim.domain.user.controller;

import com.dev.moim.domain.user.dto.ProfileDetailGetResponse;
import com.dev.moim.domain.user.dto.ProfileListGetResponse;
import com.dev.moim.domain.user.dto.ProfileResponse;
import com.dev.moim.domain.user.dto.ProfileUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @GetMapping("")
    @Operation(summary = "마이페이지 조회", description = "마이페이지 메인 화면 조회 기능 입니다.")
    public ProfileResponse getProfile() {
        return null;
    }

    @GetMapping("/list")
    @Operation(summary = "(마이페이지 : 내 프로필 관리) 내 프로필 목록 조회", description = "유저의 프로필들을 조회할 수 있는 <내 프로필 목록> 조회 기능 입니다.")
    public ProfileListGetResponse getProfileList() {
        return null;
    }

    @GetMapping("/detail")
    @Operation(summary = "(마이페이지 : 내 프로필 관리 : 내 프로필 목록 조회) 내 프로필 상세 조회",
            description = "유저의 특정 프로필을 상세 조회하는 기능 입니다. '모임 평가 별점'과 '가입 모임'은 유저의 모든 프로필에서 동일하게 표시되어 프로필 별 설정이 불가능 합니다.")
    public ProfileDetailGetResponse getDetailProfile() {
        return null;
    }

    @PostMapping("/update")
    @Operation(summary = "내 프로필 수정", description = "유저의 프로필을 수정하는 기능 입니다.")
    public void updateProfile(
            @RequestBody ProfileUpdateRequest request
    ) {
    }
}
