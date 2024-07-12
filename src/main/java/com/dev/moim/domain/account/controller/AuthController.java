package com.dev.moim.domain.account.controller;

import com.dev.moim.domain.account.dto.*;
import com.dev.moim.domain.account.service.AuthService;
import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.security.annotation.ExtractToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "유저 인증 관련 컨트롤러")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary="회원 가입 API", description="회원 가입 성공 시, 자동 로그인을 위해 유저 계정 정보 반환" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "회원 가입 성공"),
    })
    public BaseResponse<JoinResponse> join(@RequestBody JoinRequest request) {
        return BaseResponse.onSuccess(authService.join(request));
    }

    @PostMapping("/login")
    @Operation(summary="로그인 API", description="로그인 성공 시, token 정보 반환" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "로그인 성공"),
    })
    public BaseResponse<TokenResponse> login(@RequestBody @Valid LoginRequest request) {

        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/reissueToken")
    @Operation(summary="토큰 재발급 API", description="AccessToken의 유효 기간이 만료된 경우, Authorization 헤더에 RefreshToken을 담아서 요청을 보내면 AccessToken과 RefreshToken 재발급.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "토큰 재발급 성공"),
    })
    public BaseResponse<TokenResponse> reissueToken(@ExtractToken @Parameter(name = "refreshToken", hidden = true) String refreshToken) {
        return BaseResponse.onSuccess(authService.reissueToken(refreshToken));
    }

    @PostMapping("/signOut")
    @Operation(summary="로그아웃 API", description="로그아웃 후, 기존 유효한 토큰 무효화" )
    public BaseResponse<?> signOut(@ExtractToken String refreshToken) {
        authService.logout(refreshToken);
        return BaseResponse.onSuccess("로그아웃 성공");
    }

    @PostMapping("/oauth/kakao")
    @Operation(summary="카카오 로그인 API", description="카카오 accessToken을 이용하여 유저 정보 받아 저장하고 앱의 토큰 반환" )
    public BaseResponse<TokenResponse> kakaoSignIn(
            @RequestBody AuthRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/oauth/google")
    @Operation(summary="구글 로그인 API", description="구글 accessToken을 이용하여 유저 정보 받아 저장하고 앱의 토큰 반환" )
    public BaseResponse<TokenResponse> googleSignIn(
            @RequestBody AuthRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/oauth/apple")
    @Operation(summary="애플 로그인 API", description="애플 accessToken을 이용하여 유저 정보 받아 저장하고 앱의 토큰 반환" )
    public BaseResponse<TokenResponse> appleSignIn(
            @RequestBody AppleAuthRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }
}
