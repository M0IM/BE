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
    PAGE_UNDER_ZERO(HttpStatus.BAD_REQUEST, "COMMON_001", "페이지는 0이상이어야 합니다."),
    MULTIPLE_FIELD_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "COMMON_002", "입력된 정보에 오류가 있습니다. 필드별 오류 메시지를 참조하세요."),
    NO_MATCHING_ERROR_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_003", "서버 에러. 일치하는 errorStatus를 찾을 수 없습니다."),
    REQUEST_BODY_INVALID(HttpStatus.BAD_REQUEST, "COMMON_004", "요청 본문을 읽을 수 없습니다. 빈 문자열 또는 null이 있는지 확인해주세요."),

    // Redis 관련 에러
    REDIS_CONNECTION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "REDIS_001", "Redis 연결에 실패했습니다. 관리자에게 문의 바랍니다."),

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
    USER_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "AUTH_013", "유저 인증에 실패했습니다."),
    USER_INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, "AUTH_014", "권한이 부족한 사용자 입니다."),
    MISSING_AUTHORIZATION_HEADER(HttpStatus.UNAUTHORIZED, "AUTH_015", "Authorization 헤더가 비어있습니다."),
    PROVIDER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_016", "지원하지 않는 로그인 provider 입니다."),
    ID_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_017", "ID 토큰이 만료되었습니다."),
    ID_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_018", "유효하지 않은 ID 토큰 입니다."),
    LOGOUT_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_019", "로그아웃된 access 토큰 입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_020", "존재하지 않는 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH_021", "비밀번호 조건에 맞지 않습니다."),
    OAUTH_ACCOUNT_DUPLICATION(HttpStatus.BAD_REQUEST, "AUTH_022", "이미 가입한 소셜 계정입니다."),
    OAUTH_SECRET_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_023", "OAuth 관련 환경 변수가 누락되었습니다."),
    GET_OAUTH_USER_INFO_FAIL(HttpStatus.BAD_REQUEST, "AUTH_024", "OAuth 유저 정보 요청에 실패했습니다."),
    PROVIDER_ID_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_025", "providerId가 누락되었습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, "AUTH_026", "잘못된 요청 본문입니다."),
    INVALID_REQUEST_HEADER(HttpStatus.BAD_REQUEST, "AUTH_027", "잘못된 요청 헤더입니다."),
    USER_UNREGISTERED(HttpStatus.UNAUTHORIZED, "AUTH_028", "존재하지 않는 계정입니다. 회원가입을 진행해주세요."),
    OIDC_PUBLIC_KEY_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH_029", "OIDC ID 토큰 공개키를 받아오는데 실패했습니다."),
    HTTP_REQUEST_NULL(HttpStatus.BAD_REQUEST, "AUTH_030", "HttpServletRequest가 null입니다."),

    // Plan 관련
    INDIVIDUAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN_001", "존재하지 않는 개인 일정 입니다."),
    NOT_INDIVIDUAL_PLAN_OWNER(HttpStatus.UNAUTHORIZED, "PLAN_002", "해당 일정의 작성자가 아닙니다."),
    ALREADY_PARTICIPATE_IN_PLAN(HttpStatus.BAD_REQUEST, "PLAN_003", "이미 해당 모임 일정에 참여 신청했습니다."),
    USER_NOT_PART_OF_PLAN(HttpStatus.BAD_REQUEST, "PLAN_004", "해당 일정에 참여 신청하지 않았습니다."),
    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "PLAN_005", "존재하지 않는 일정입니다."),
    PLAN_EDIT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "PLAN_006", "모임 일정 수정, 삭제 권한이 없는 유저입니다." ),

    // Email 관련
    EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "EMAIL_001", "이메일 전송에 실패했습니다."),
    INCORRECT_EMAIL_CODE(HttpStatus.UNAUTHORIZED, "EMAIL_002", "이메일 인증 코드가 일치하지 않습니다."),
    EMAIL_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "EMAIL_003", "유저 이메일에 해당하는 이메일 코드가 저장되어있지 않습니다. 재요청을 시도해주세요."),
    EMAIL_AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "EMAIL_004", "이메일 인증에 실패했습니다."),

    // Moim 관련
    MOIM_NOT_FOUND(HttpStatus.NOT_FOUND, "MOIM_001", "모임을 찾을 수 없습니다."),
    MOIM_NOT_ADMIN(HttpStatus.UNAUTHORIZED, "MOIM_002", "모임 관리자 회원이 아닙니다."),
    INVALID_MOIM_MEMBER(HttpStatus.FORBIDDEN, "MOIM_003", "모임의 멤버가 아닙니다."),
    IS_MOIM_OWNER(HttpStatus.BAD_REQUEST, "MOIM_004", "모임장 권한이 있는 유저입니다. 권한을 위임해주세요."),
    PLAN_WRITER_NOT_FOUND(HttpStatus.NOT_FOUND, "MOIM_005", "해당 일정의 작성자를 찾을 수 없습니다."),
    USER_NOT_MOIM_JOIN(HttpStatus.UNAUTHORIZED, "MOIM_006", "모임의 회원이 아닙니다."),
    USER_NOT_MOIM_ADMIN(HttpStatus.FORBIDDEN, "MOIM_007", "모임의 관리자가 아닙니다."),
    VIDEO_ERROR(HttpStatus.NOT_FOUND, "MOIM_008", "해당 모임의 관리자가 없거나 관리자의 프로필이 존재하지 않습니다."),
    MOIM_OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "MOIM_009", "모임장 회원을 찾을 수 없습니다."),
    ALREADY_REQUEST(HttpStatus.FORBIDDEN, "MOIM_010", "이미 신청한 모임 입니다.."),
    NOT_REQUEST_JOIN(HttpStatus.NOT_FOUND, "MOIM_011", "신청하지 않은 모임입니다."),
    USER_MOIM_NOT_FOUND(HttpStatus.NOT_FOUND, "MOIM_012", "user moim을 찾을 수 없습니다."),
    OWNER_NOT_EXIT(HttpStatus.NOT_FOUND, "MOIM_013", "owner는 모임을 나갈 수 없습니다."),

    // UserProfile 관련
    USER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "USERPROFILE_001", "프로필을 찾을 수 없습니다."),
    USER_PROFILE_NOT_FOUND_MAIN(HttpStatus.NOT_FOUND, "USERPROFILE_002", "메인 프로필을 찾을 수 없습니다."),
    NOT_USER_PROFILE_OWNER(HttpStatus.UNAUTHORIZED, "USERPROFILE_003", "해당 유저의 프로필이 아닙니다."),
    USER_PROFILE_IN_USE(HttpStatus.FORBIDDEN, "USERPROFILE_004", "해당 프로필을 사용 중인 모임이 있습니다."),
    CANNOT_DELETE_MAIN_USER_PROFILE(HttpStatus.FORBIDDEN, "USERPROFILE_005", "대표 프로필은 삭제할 수 없습니다. 대표 프로필을 변경해주세요."),

    // page 관련
    NOT_VALID_CURSOR(HttpStatus.BAD_REQUEST, "PAGE_001", "커서 값이 유효하지 않습니다."),
    NOT_VALID_TAKE(HttpStatus.BAD_REQUEST, "PAGE_002", "take 값이 유효하지 않습니다."),
    NOT_VALID_PAGE(HttpStatus.BAD_REQUEST, "PAGE_003", "page 값이 유효하지 않습니다."),
    NOT_VALID_SIZE(HttpStatus.BAD_REQUEST, "PAGE_004", "size 값이 유효하지 않습니다."),

    // Post
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_001", "POST를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_002", "COMMENT를 찾을 수 없습니다."),
    BLOCK_POST(HttpStatus.FORBIDDEN, "POST_003", "해당 게시물이 차단 되어있습니다."),
    NOT_MY_POST(HttpStatus.FORBIDDEN, "POST_004", "해당 작성물이 자신의 것이 아닙니다."),
    ALREADY_COMMENT_DELETE(HttpStatus.FORBIDDEN, "POST_005", "이미 삭제된 댓글입니다."),
    NOT_INCLUDE_POST(HttpStatus.FORBIDDEN, "POST_006", "해당 댓글이 게시물에 포함되어 있지 않습니다."),
    NOT_ANNOUNCEMENT_POST(HttpStatus.FORBIDDEN, "POST_007", "해당 게시물은 공지사항이 아닙니다."),

    // 채팅 관련 에러
    INVALID_CHAT_SCROLL(HttpStatus.BAD_REQUEST, "CHAT4001", "더 이상 채팅이 존재하지 않습니다."),
    CHAT_NOT_SEND(HttpStatus.BAD_REQUEST, "CHAT4002", "전송 중 오류가 발생하였습니다."),
    INVALID_CHAT_FORMAT(HttpStatus.BAD_REQUEST, "CHAT4003", "채팅 형식이 유효하지 않습니다."),

    // 채팅방 관련 에러
    INVALID_CHATROOM_SCROLL(HttpStatus.BAD_REQUEST, "CHATROOM4001", "더 이상 채팅방이 존재하지 않습니다."),
    CHATROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHATROOM4002", "해당 채팅방이 존재하지 않습니다."),
    ALREADY_JOIN_CHATROOM(HttpStatus.BAD_REQUEST, "CHATROOM4003", "이미 채팅방에 들어와있습니다."),
    FIRST_JOIN_CHATROOM(HttpStatus.BAD_REQUEST, "CHATROOM4004", "채팅방에 먼저 들어와 있어야 합니다."),
    NOT_JOIN_CHATROOM(HttpStatus.BAD_REQUEST, "CHATROOM4005", "채팅방에 가입되있지 않습니다."),
    FAILED_ENTER_CHATROOM(HttpStatus.INTERNAL_SERVER_ERROR, "CHATROOM5001", "채팅 방 들어가기에 실패하였습니다"),
    FAILED_EXIT_CHATROOM(HttpStatus.INTERNAL_SERVER_ERROR, "CHATROOM5002", "채팅 방 나가기에 실패하였습니다"),
    INVALID_CHATROOM(HttpStatus.INTERNAL_SERVER_ERROR, "CHATROOM5003", "정상적인 채팅방이 아닙니다. 새로운 채팅방을 다시 생성해주세요"),
    OVERLAP_JOIN_USER(HttpStatus.BAD_REQUEST, "CHATROOM4006", "들어오는 user와 participant의 user의 id가 같으면 안됩니다."),

    // FeignClient 관련
    FEIGN_400_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEI_001", "FeignClient 400번대 에러 발생"),
    FEIGN_500_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEI_002", "FeignClient 500번대 에러 발생"),
    FEIGN_UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FEI_003", "FeignClient 알 수 없는 에러 발생"),

    // FCM 관련 에러
    FCM_NOT_VALID(HttpStatus.UNAUTHORIZED, "FCM_001", "FCM 토큰이 유효하지 않습니다.."),
    FCM_TOKEN_REQUIRED(HttpStatus.BAD_REQUEST, "FCM_002", "FCM 토큰이 누락되었습니다."),

    // Review 관련
    SELF_REVIEW_FORBIDDEN(HttpStatus.FORBIDDEN, "REVIEW_001", "유저 본인에게 리뷰를 남길 수 없습니다."),

    // Todo 관련
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_001", "Todo를 찾을 수 없습니다."),
    NOT_TODO_ASSIGNEE(HttpStatus.UNAUTHORIZED, "TODO_002", "해당 유저에게 부여된 todo가 아닙니다."),
    TODO_STATUS_SAME(HttpStatus.BAD_REQUEST, "TODO_003", "업데이트 요청한 todo status가 기존 status와 동일합니다."),
    TODO_ASSIGNEE_NULL(HttpStatus.BAD_REQUEST, "TODO_004", "Todo를 할당받을 유저를 지정하지 않았습니다."),
    TODO_ASSIGNEE_NOT_MATCH(HttpStatus.BAD_REQUEST, "TODO_005", "전체 선택인 경우 특정 assignee를 지정할 수 없습니다."),
    TODO_DUE_DATE_EXPIRED(HttpStatus.BAD_REQUEST, "TODO_006", "마감 기한이 지난 Todo입니다."),
    TODO_INVALID_STATE_REQUEST(HttpStatus.BAD_REQUEST, "TODO_007", "해당 todo status로 변경할 수 없습니다."),
    IS_ALREADY_TODO_ASSIGNEE(HttpStatus.BAD_REQUEST, "TODO_008", "이미 todo를 할당받은 멤버를 지정했습니다."),
    INVALID_TODO_DUE_DATE(HttpStatus.BAD_REQUEST, "TODO_009", "todo 마감 기한을 현재 날짜 이전으로 수정할 수 없습니다.")
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
