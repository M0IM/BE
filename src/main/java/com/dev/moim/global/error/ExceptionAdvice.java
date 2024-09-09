package com.dev.moim.global.error;

import com.dev.moim.global.common.BaseResponse;
import com.dev.moim.global.common.code.ErrorReasonDTO;
import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.feign.dto.DiscordMessage;
import com.dev.moim.global.error.feign.service.DiscordClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.NO_MATCHING_ERROR_STATUS;
import static com.dev.moim.global.common.code.status.ErrorStatus.REQUEST_BODY_INVALID;

@Slf4j
@RestControllerAdvice(annotations = {RestController.class})
@RequiredArgsConstructor
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    private final DiscordClient discordClient;
    private final Environment environment;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> validation(ConstraintViolationException e, WebRequest request) {

        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        ErrorStatus errorStatus = mapToErrorStatus(errorMessage);

        if (errorStatus == null) {
            throw new IllegalArgumentException(String.valueOf(NO_MATCHING_ERROR_STATUS));
        }

        return handleExceptionInternalConstraint(e, errorStatus, HttpHeaders.EMPTY,request);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
            errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
        });

        ErrorStatus errorStatus = mapToErrorStatus(errors.values().iterator().next());

        if (errors.size() != 1 || errorStatus == null) {
            errorStatus = ErrorStatus.MULTIPLE_FIELD_VALIDATION_ERROR;
        }

        return handleExceptionInternalArgs(ex, HttpHeaders.EMPTY, errorStatus, request, errors);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(Exception e, WebRequest request) {

        if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            sendDiscordAlarm(e, request);
        }

        return handleExceptionInternalFalse(
                e,
                ErrorStatus._INTERNAL_SERVER_ERROR,
                HttpHeaders.EMPTY,
                ErrorStatus._INTERNAL_SERVER_ERROR.getHttpStatus(),
                request,
                e.getMessage());
    }

    @ExceptionHandler(value = GeneralException.class)
    public ResponseEntity<Object> onThrowException(GeneralException generalException, HttpServletRequest request) {

        ErrorReasonDTO errorReasonHttpStatus = generalException.getErrorReasonHttpStatus();

        return handleExceptionInternal(generalException,errorReasonHttpStatus,null,request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        BaseResponse<Object> body = BaseResponse.onFailure(ErrorStatus._BAD_REQUEST.getCode(), REQUEST_BODY_INVALID.getMessage(), null);

        return handleExceptionInternal(e, body, HttpHeaders.EMPTY, ErrorStatus._BAD_REQUEST.getHttpStatus(), request);
    }

    private ResponseEntity<Object> handleExceptionInternal(Exception e, ErrorReasonDTO reason,
                                                           HttpHeaders headers, HttpServletRequest request) {

        BaseResponse<Object> body = BaseResponse.onFailure(reason.getCode(),reason.getMessage(),null);

        WebRequest webRequest = new ServletWebRequest(request);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                reason.getHttpStatus(),
                webRequest
        );
    }

    private ResponseEntity<Object> handleExceptionInternalFalse(Exception e, ErrorStatus errorCommonStatus,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request, String errorPoint) {

        BaseResponse<Object> body = BaseResponse.onFailure(errorCommonStatus.getCode(),errorCommonStatus.getMessage(),errorPoint);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                status,
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalArgs(Exception e, HttpHeaders headers, ErrorStatus errorCommonStatus,
                                                               WebRequest request, Map<String, String> errorArgs) {

        BaseResponse<Object> body = BaseResponse.onFailure(errorCommonStatus.getCode(),errorCommonStatus.getMessage(),errorArgs);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }

    private ResponseEntity<Object> handleExceptionInternalConstraint(Exception e, ErrorStatus errorCommonStatus,
                                                                     HttpHeaders headers, WebRequest request) {

        BaseResponse<Object> body = BaseResponse.onFailure(errorCommonStatus.getCode(), errorCommonStatus.getMessage(), null);

        return super.handleExceptionInternal(
                e,
                body,
                headers,
                errorCommonStatus.getHttpStatus(),
                request
        );
    }

    private void sendDiscordAlarm(Exception e, WebRequest request) {
        discordClient.sendAlarm(createMessage(e, request));
    }

    private DiscordMessage createMessage(Exception e, WebRequest request) {
        return DiscordMessage.builder()
                .content("# 🚨 에러 발생 비이이이이사아아아앙")
                .embeds(
                        List.of(
                                DiscordMessage.Embed.builder()
                                        .title("ℹ️ 에러 정보")
                                        .description(
                                                "### 🕖 발생 시간\n"
                                                        + LocalDateTime.now()
                                                        + "\n"
                                                        + "### 🔗 요청 URL\n"
                                                        + createRequestFullPath(request)
                                                        + "\n"
                                                        + "### 📄 Stack Trace\n"
                                                        + "```\n"
                                                        + getStackTrace(e).substring(0, 1000)
                                                        + "\n```")
                                        .build()))
                .build();
    }

    private String createRequestFullPath(WebRequest webRequest) {
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        String fullPath = request.getMethod() + " " + request.getRequestURL();

        String queryString = request.getQueryString();
        if (queryString != null) {
            fullPath += "?" + queryString;
        }

        return fullPath;
    }

    private String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private ErrorStatus mapToErrorStatus(String errorMessage) {
        for (ErrorStatus status : ErrorStatus.values()) {
            if (status.getMessage().equals(errorMessage)) {
                return status;
            }
        }
        return null;
    }
}
