package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.global.validation.annotation.PasswordValidation;
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
public class PasswordValidator implements ConstraintValidator<PasswordValidation, JoinRequest> {

    @Override
    public void initialize(PasswordValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(JoinRequest request, ConstraintValidatorContext context) {

        log.info("PasswordValidator");

        if (request.provider() == LOCAL) {
            String passwordPattern = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[\\W_]).{8,16}$";
            if (request.password() == null || !request.password().matches(passwordPattern)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(INVALID_PASSWORD.getMessage())
                        .addPropertyNode("password")
                        .addConstraintViolation();

                log.warn("비밀번호 조건에 맞지 않습니다.");
                return false;
            }
        }
        return true;
    }
}
