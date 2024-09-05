package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.global.validation.annotation.JoinPasswordValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.domain.account.entity.enums.Provider.LOCAL;
import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_PASSWORD;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinPasswordValidator implements ConstraintValidator<JoinPasswordValidation, JoinRequest> {

    @Override
    public void initialize(JoinPasswordValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(JoinRequest request, ConstraintValidatorContext context) {
        if (request.provider() == LOCAL && !isPasswordValid(request.password(), context)) {
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password, ConstraintValidatorContext context) {
        String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,16}$";

        if (password == null || !password.matches(passwordPattern)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INVALID_PASSWORD.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
