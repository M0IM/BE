package com.dev.moim.global.common.code.status;

import com.dev.moim.global.common.code.BaseErrorCode;
import com.dev.moim.global.common.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 기본 에러
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 공통 에러
    PAGE_UNDER_ZERO(HttpStatus.BAD_REQUEST, "COMM_001", "페이지는 0이상이어야 합니다."),

    // S3 관련
    S3_OBJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "S3_001", "S3 오브젝트를 찾을 수 없습니다."),
    S3_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3_002", "S3 업로드 실패"),

    // Auth 관련
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_001", "토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_002", "토큰이 유효하지 않습니다."),
    OAUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_003", "OAuth 토큰이 유효하지 않습니다."),
    INVALID_LOGIN_REQUEST(HttpStatus.UNAUTHORIZED, "AUTH_004", "올바른 이메일이나 패스워드가 아닙니다."),
    INVALID_REQUEST_INFO(HttpStatus.UNAUTHORIZED, "AUTH_005", "카카오 정보 불러오기에 실패하였습니다."),
    USER_PROPERTY_NOT_FOUND(HttpStatus.BAD_REQUEST,"AUTH_006", "카카오 앱에 설정되어 있지 않은 사용자 프로퍼티를 요청하였습니다." ),
    NOT_EQUAL_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_007", "리프레시 토큰이 다릅니다."),
    NOT_CONTAIN_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_008", "해당하는 토큰이 저장되어있지 않습니다."),
    BAD_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_009", "비밀번호를 잘못 입력했습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH_010", "인증에 실패했습니다."),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "AUTH_011", "이미 가입한 메일 입니다."),
    INVALID_GENDER(HttpStatus.BAD_REQUEST, "AUTH_012", "올바르지 않은 gender 입력값 입니다."),
    USER_AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH_013", "인증이 필요한 사용자입니다."),
    USER_INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "AUTH_014", "권한이 부족한 사용자 입니다."),
    MISSING_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "AUTH_015", "Authorization 헤더가 비어있습니다."),
    OAUTH_PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_016", "지원하지 않는 소셜 로그인 provider 입니다."),
    ID_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_018", "만료된 ID 토큰 입니다."),
    ID_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_019", "유효하지 않은 ID 토큰 입니다."),

    // Moim 관련
    MOIM_NOT_FOUND(HttpStatus.NOT_FOUND, "MOIM_001", "모임을 찾을 수 없습니다."),
    MOIM_NOT_ADMIN(HttpStatus.UNAUTHORIZED, "MOIM_002", "모임 관리자 회원이 아닙니다."),

    // User 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_004", "존재하지 않는 사용자입니다."),

    // Recreation 관련
    SEARCH_CONDITION_INVALID(HttpStatus.BAD_REQUEST, "RECR_001", "검색 조건이 하나라도 존재해야 합니다."),
    RECREATION_NOT_FOUND(HttpStatus.NOT_FOUND, "RECR_002", "존재하지 않는 레크레이션입니다."),

    // RecreationReview 관련
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REV_001", "존재하지 않는 리뷰입니다."),

    // Flow 관련
    FLOW_NOT_FOUND(HttpStatus.NOT_FOUND, "FLO_001", "존재하지 않는 플로우입니다."),
    FLOW_DELETE_UNAUTHORIZED(HttpStatus.FORBIDDEN, "FLOW_002", "삭제 권한이 없습니다."),

    // FeignClient 관련
    FEIGN_400_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEI_001", "FeignClient 400번대 에러 발생"),
    FEIGN_500_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEI_002", "FeignClient 500번대 에러 발생"),
    FEIGN_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEI_003", "FeignClient 알 수 없는 에러 발생"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder().message(message).code(code).isSuccess(false).build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
