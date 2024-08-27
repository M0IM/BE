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

import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_NOT_FOUND;
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

        boolean isValidPlan = calenderQueryService.existsByPlanId(planId);
        if (!isValidPlan) {
            addConstraintViolation(context, PLAN_NOT_FOUND.toString(), "planId");
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isExistUserPlan = calenderQueryService.existsByUserIdAndPlanId(Long.valueOf(authentication.getName()), planId);
        if (!isExistUserPlan) {
            addConstraintViolation(context, USER_NOT_PART_OF_PLAN.toString(), "userId");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}
