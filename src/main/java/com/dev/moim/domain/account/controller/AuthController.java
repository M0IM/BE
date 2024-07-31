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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "유저 인증 관련 컨트롤러")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/join")
    @Operation(summary="회원 가입 API", description="로그인 타입을 입력해주세요. \n [Provider] LOCAL(일반 로그인), KAKAO, APPLE, GOOGLE, NAVER" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON201", description = "요청 성공 및 리소스 생성됨"),
            @ApiResponse(responseCode = "AUTH_011", description = "이미 가입한 메일 입니다.")
    })
    public BaseResponse<TokenResponse> join(@RequestBody JoinRequest request) {
        return BaseResponse.onSuccess(authService.join(request));
    }

    @PostMapping("/login")
    @Operation(summary="일반 로그인 API", description="로그인 성공 시, token 정보 반환 (개발용)" )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH_009", description = "비밀번호를 잘못 입력했습니다."),
            @ApiResponse(responseCode = "AUTH_010", description = "인증에 실패했습니다."),
            @ApiResponse(responseCode = "AUTH_021", description = "존재하지 않는 사용자입니다.")
    })
    public BaseResponse<TokenResponse> login(@RequestBody LoginRequest request) {
        return BaseResponse.onSuccess(null);
    }

    @GetMapping("/reissueToken")
    @Operation(summary="토큰 재발급 API", description="AccessToken의 유효 기간이 만료된 경우, Authorization 헤더에 RefreshToken을 담아서 요청을 보내면 AccessToken과 RefreshToken 재발급.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH_001", description = "토큰이 만료되었습니다."),
            @ApiResponse(responseCode = "AUTH_002", description = "토큰이 유효하지 않습니다."),
            @ApiResponse(responseCode = "AUTH_021", description = "존재하지 않는 사용자입니다.")
    })
    public BaseResponse<TokenResponse> reissueToken(@ExtractToken @Parameter(name = "refreshToken", hidden = true) String refreshToken) {
        return BaseResponse.onSuccess(authService.reissueToken(refreshToken));
    }

    @GetMapping("/logout")
    @Operation(summary="로그아웃 API", description="로그아웃 후, 기존 유효한 토큰 무효화 (개발용)" )
    public BaseResponse<?> signOut(
            @ExtractToken @Parameter(name = "accessToken", hidden = true) String accessToken
    ) {
        return BaseResponse.onSuccess("로그아웃 성공");
    }

    @PostMapping("/oAuth")
    @Operation(summary="소셜 로그인 API", description="소셜 로그인 타입을 입력해주세요.\n [Provider] KAKAO, APPLE, GOOGLE, NAVER (개발용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "AUTH_001", description = "신규 유저 입니다. 회원가입을 진행해주세요."),
            @ApiResponse(responseCode = "AUTH_016", description = "지원하지 않는 로그인 provider 입니다."),
            @ApiResponse(responseCode = "AUTH_018", description = "만료된 ID 토큰 입니다."),
            @ApiResponse(responseCode = "AUTH_019", description = "유효하지 않은 ID 토큰 입니다."),
    })
    public BaseResponse<TokenResponse> oAuthLogin(
            @RequestBody OAuthLoginRequest request
    ) {
        return BaseResponse.onSuccess(null);
    }

    @PostMapping("/emails/send")
    @Operation(summary="이메일 인증 코드 전송 요청 API", description="이메일 인증 번호 전송을 요청하는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "EMAIL_001", description = "이메일 전송에 실패했습니다.")
    })
    public BaseResponse<EmailVerificationCodeDTO> sendCode(@RequestBody EmailDTO request) {
        return BaseResponse.onSuccess(authService.sendCode(request));
    }

    @PostMapping("/emails/verify")
    @Operation(summary="이메일 코드 인증 요청 API", description="이메일 인증 코드 일치 여부를 확인해주는 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다."),
            @ApiResponse(responseCode = "EMAIL_002", description = "이메일 인증 코드가 일치하지 않습니다."),
            @ApiResponse(responseCode = "EMAIL_003", description = "유저 이메일에 해당하는 이메일 코드가 저장되어있지 않습니다. 재요청을 시도해주세요."),
            @ApiResponse(responseCode = "EMAIL_004", description = "이메일 인증에 실패했습니다.")
    })
    public BaseResponse<EmailVerificationResultDTO> verifyCode(@RequestBody EmailVerificationCodeDTO request) {
        return BaseResponse.onSuccess(authService.verifyCode(request));
    }
}
