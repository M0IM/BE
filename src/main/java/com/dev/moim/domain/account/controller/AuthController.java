package com.dev.moim.domain.account.controller;

import com.dev.moim.domain.account.dto.*;
import com.dev.moim.global.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/signUp")
    @Operation(summary="회원 가입", description="회원 가입 성공 시, 자동 로그인을 위해 유저 계정 정보 반환" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "CREATED", description = "회원 가입 성공"),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "회원 가입 실패")
    })
    public BaseResponse<SignUpResponse> signUp(@RequestBody SignUpRequest request) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/signIn")
    @Operation(summary="로그인", description="로그인 성공 시, token 정보 반환" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "CREATED", description = "로그인 성공"),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "로그인 실패")
    })
    public BaseResponse<TokenResponse> signIn(@RequestBody SignInRequest request) {

        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/reissueToken")
    @Operation(summary="토큰 재발급", description="accessToken의 유효 기간이 만료된 경우, Authorization 헤더에 refreshToken을 담아서 요청을 보내면 accessToken을 재발급." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "CREATED", description = "토큰 재발급 성공"),
            @ApiResponse(responseCode = "BAD_REQUEST", description = "토큰 재발급 실패")
    })
    public BaseResponse<ReissueTokenResponse> reissueToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/signOut")
    @Operation(summary="로그아웃", description="로그아웃 후, 기존 유효한 토큰 무효화" )
    public BaseResponse<?> signOut(
            @RequestHeader("Authorization") String accessToken
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/oauth/kakao")
    @Operation(summary="카카오 로그인", description="카카오 accessToken을 이용하여 유저 정보 받아 저장하고 앱의 토큰 반환" )
    public BaseResponse<TokenResponse> kakaoSignIn(
            @RequestBody AuthRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/oauth/google")
    @Operation(summary="구글 로그인", description="구글 accessToken을 이용하여 유저 정보 받아 저장하고 앱의 토큰 반환" )
    public BaseResponse<TokenResponse> googleSignIn(
            @RequestBody AuthRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/oauth/apple")
    @Operation(summary="애플 로그인", description="애플 accessToken을 이용하여 유저 정보 받아 저장하고 앱의 토큰 반환" )
    public BaseResponse<TokenResponse> appleSignIn(
            @RequestBody AppleAuthRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }
}
