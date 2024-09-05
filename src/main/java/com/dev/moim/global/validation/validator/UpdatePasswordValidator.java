package com.dev.moim.global.validation.validator;

import com.dev.moim.global.validation.annotation.UpdatePasswordValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_PASSWORD;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdatePasswordValidator implements ConstraintValidator<UpdatePasswordValidation, String> {

    @Override
    public void initialize(UpdatePasswordValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
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
