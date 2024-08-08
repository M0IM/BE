package com.dev.moim.global.validation.validator;

import com.dev.moim.global.common.code.status.ErrorStatus;
import com.dev.moim.global.validation.annotation.CheckTakeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckTakeValidator implements ConstraintValidator<CheckTakeValidation, Integer> {
    @Override
    public void initialize(CheckTakeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.NOT_VALID_TAKE.toString())
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
