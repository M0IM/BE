package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.entity.IndividualPlan;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.IndividualPlanValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.INDIVIDUAL_PLAN_NOT_FOUND;
import static com.dev.moim.global.common.code.status.ErrorStatus.NOT_INDIVIDUAL_PLAN_OWNER;

@Slf4j
@Component
@RequiredArgsConstructor
public class IndividualPlanValidator implements ConstraintValidator<IndividualPlanValidation, Long> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(IndividualPlanValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long individualPlanId, ConstraintValidatorContext context) {

        Optional<IndividualPlan> individualPlan = userQueryService.findUserByPlanId(individualPlanId);
        if (individualPlan.isEmpty()) {
            addConstraintViolation(context, INDIVIDUAL_PLAN_NOT_FOUND.getMessage());
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!Objects.equals(individualPlan.get().getUser().getId(), Long.valueOf(authentication.getName()))) {
            addConstraintViolation(context, NOT_INDIVIDUAL_PLAN_OWNER.getMessage());
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorStatus) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorStatus)
                .addConstraintViolation();
    }
}
