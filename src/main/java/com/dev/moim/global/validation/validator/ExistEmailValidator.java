package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.ExistEmailValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ExistEmailValidator implements ConstraintValidator<ExistEmailValidation, String> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(ExistEmailValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        boolean isExistEmail = userQueryService.isExistEmail(email);

        if (!isExistEmail){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(USER_NOT_FOUND.toString())
                    .addPropertyNode("email")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
