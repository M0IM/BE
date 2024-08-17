package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.error.handler.IndividualPlanException;
import com.dev.moim.global.validation.annotation.IndividualPlanValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Long userId = userQueryService.findUserByPlanId(individualPlanId);

            if (!Objects.equals(userId, Long.valueOf(authentication.getName()))) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(NOT_INDIVIDUAL_PLAN_OWNER.toString())
                        .addPropertyNode("individualPlanId")
                        .addConstraintViolation();
                return false;
            }
            return true;
        } catch (IndividualPlanException e) {

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INDIVIDUAL_PLAN_NOT_FOUND.toString())
                    .addPropertyNode("individualPlanId")
                    .addConstraintViolation();
            return false;
        }
    }
}
