package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.global.validation.annotation.PlanValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanValidator implements ConstraintValidator<PlanValidation, Long> {

    private final CalenderQueryService calenderQueryService;

    @Override
    public void initialize(PlanValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long planId, ConstraintValidatorContext context) {

        boolean isValidPlan = calenderQueryService.existsByPlanId(planId);

        if (!isValidPlan) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(PLAN_NOT_FOUND.getMessage())
                    .addPropertyNode("planId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
