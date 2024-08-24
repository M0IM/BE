package com.dev.moim.global.validation.validator;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.error.handler.AuthException;
import com.dev.moim.global.firebase.service.FcmQueryService;
import com.dev.moim.global.validation.annotation.FcmTokenValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.FCM_NOT_VALID;
import static com.dev.moim.global.common.code.status.ErrorStatus.FCM_TOKEN_REQUIRED;

@Component
@RequiredArgsConstructor
public class FcmTokenValidator implements ConstraintValidator<FcmTokenValidation, String> {

    private final FcmQueryService fcmQueryService;

    @Override
    public void initialize(FcmTokenValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String fcmToken, ConstraintValidatorContext context) {

        if (fcmToken == null || fcmToken.trim().isEmpty()) {
            addConstraintViolation(context, FCM_TOKEN_REQUIRED);
            return false;
        }

        try {
            fcmQueryService.isTokenValid("MOIM", fcmToken);
        } catch (AuthException e) {
            addConstraintViolation(context, FCM_NOT_VALID);
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, ErrorStatus errorStatus) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorStatus.toString())
                .addConstraintViolation();
    }
}
