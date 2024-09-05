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

        String providerId = request.providerId();
        if (providerId == null || providerId.isEmpty()) {
            addConstraintViolation(context, PROVIDER_ID_NOT_FOUND.getMessage());
            return false;
        }

        boolean isDuplicated = userQueryService.existsByProviderAndProviderId(request.provider(), request.providerId());
        if (isDuplicated) {
            addConstraintViolation(context, OAUTH_ACCOUNT_DUPLICATION.getMessage());
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
