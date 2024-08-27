package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.ExistUserValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class ExistUserValidator implements ConstraintValidator<ExistUserValidation, Long> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(ExistUserValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long userId, ConstraintValidatorContext context) {
        Optional<User> user = userQueryService.findUserById(userId);

        if (user.isEmpty()){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(USER_NOT_FOUND.toString())
                    .addPropertyNode("userId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
