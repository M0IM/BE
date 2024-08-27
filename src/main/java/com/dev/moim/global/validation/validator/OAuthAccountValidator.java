package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.OAuthAccountValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.domain.account.entity.enums.Provider.LOCAL;
import static com.dev.moim.global.common.code.status.ErrorStatus.OAUTH_ACCOUNT_DUPLICATION;
import static com.dev.moim.global.common.code.status.ErrorStatus.PROVIDER_ID_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAccountValidator implements ConstraintValidator<OAuthAccountValidation, JoinRequest> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(OAuthAccountValidation constraintAnnotation) {}

    @Override
    public boolean isValid(JoinRequest request, ConstraintValidatorContext context) {
        if (request.provider() == LOCAL) {
            return true;
        }

        if (isProviderIdInvalid(request.providerId(), context)) {
            log.warn("회원가입 실패 : providerId 누락");
            return false;
        }

        if (isProviderIdDuplicated(request, context)) {
            log.warn("회원가입 실패 : 이미 가입된 소셜 계정");
            return false;
        }

        return true;
    }

    private boolean isProviderIdInvalid(String providerId, ConstraintValidatorContext context) {
        if (providerId == null || providerId.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(PROVIDER_ID_NOT_FOUND.toString())
                    .addPropertyNode("providerId")
                    .addConstraintViolation();
            return true;
        }
        return false;
    }

    private boolean isProviderIdDuplicated(JoinRequest request, ConstraintValidatorContext context) {
        boolean isDuplicated = userQueryService.existsByProviderAndProviderId(request.provider(), request.providerId());
        if (isDuplicated) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(OAUTH_ACCOUNT_DUPLICATION.toString())
                    .addPropertyNode("providerId")
                    .addConstraintViolation();
        }
        return isDuplicated;
    }
}
