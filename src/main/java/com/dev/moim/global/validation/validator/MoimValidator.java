package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.repository.MoimRepository;
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

    private final MoimRepository moimRepository;

    @Override
    public void initialize(MoimValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long moimId, ConstraintValidatorContext context) {

        boolean isValidMoim = moimRepository.existsById(moimId);

        if (!isValidMoim) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(MOIM_NOT_FOUND.toString())
                    .addPropertyNode("moimId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
