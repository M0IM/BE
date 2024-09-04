package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.global.validation.annotation.MoimValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.MOIM_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoimValidator implements ConstraintValidator<MoimValidation, Long> {

    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(MoimValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long moimId, ConstraintValidatorContext context) {

        boolean isValidMoim = moimQueryService.existsByMoimId(moimId);

        if (!isValidMoim) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MOIM_NOT_FOUND.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
