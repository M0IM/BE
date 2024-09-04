package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.QuitValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.IS_MOIM_OWNER;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuitValidator implements ConstraintValidator<QuitValidation, User> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(QuitValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(User user, ConstraintValidatorContext context) {

        boolean isMoimOwner = userQueryService.isMoimOwner(user);

        if (isMoimOwner) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(IS_MOIM_OWNER.getMessage())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
