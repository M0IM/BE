package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.global.validation.annotation.UserPlanValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.USER_NOT_PART_OF_PLAN;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPlanValidator implements ConstraintValidator<UserPlanValidation, Long> {

    private final CalenderQueryService calenderQueryService;

    @Override
    public void initialize(UserPlanValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long planId, ConstraintValidatorContext context) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isExistUserPlan = calenderQueryService.existsByUserIdAndPlanId(
                Long.valueOf(authentication.getName()),
                planId);

        if (!isExistUserPlan) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(USER_NOT_PART_OF_PLAN.toString())
                    .addPropertyNode("userId")
                    .addPropertyNode("planId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
