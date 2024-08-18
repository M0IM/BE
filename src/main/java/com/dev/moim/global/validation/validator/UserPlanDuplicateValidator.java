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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean alreadyParticipate = calenderQueryService.existsByUserIdAndPlanId(
                Long.valueOf(authentication.getName()),
                planId);

        if (alreadyParticipate) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ALREADY_PARTICIPATE_IN_PLAN.toString())
                    .addPropertyNode("userId")
                    .addPropertyNode("planId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
