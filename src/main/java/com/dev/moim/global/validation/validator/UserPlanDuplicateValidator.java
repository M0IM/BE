package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.global.validation.annotation.UserPlanDuplicateValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.ALREADY_PARTICIPATE_IN_PLAN;
import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPlanDuplicateValidator implements ConstraintValidator<UserPlanDuplicateValidation, Long> {

    private final CalenderQueryService calenderQueryService;

    @Override
    public void initialize(UserPlanDuplicateValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long planId, ConstraintValidatorContext context) {

        boolean isValidPlan = calenderQueryService.existsByPlanId(planId);
        if (!isValidPlan) {
            addConstraintViolation(context, PLAN_NOT_FOUND.toString());
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean alreadyParticipate = calenderQueryService.existsByUserIdAndPlanId(
                Long.valueOf(authentication.getName()), planId);
        if (alreadyParticipate) {
            addConstraintViolation(context, ALREADY_PARTICIPATE_IN_PLAN.toString());
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("planId")
                .addConstraintViolation();
    }
}
