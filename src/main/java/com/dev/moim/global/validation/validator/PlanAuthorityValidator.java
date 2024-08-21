package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PlanException;
import com.dev.moim.global.validation.annotation.PlanAuthorityValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_EDIT_UNAUTHORIZED;
import static com.dev.moim.global.common.code.status.ErrorStatus.PLAN_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanAuthorityValidator implements ConstraintValidator<PlanAuthorityValidation, Long> {

    private final CalenderQueryService calenderQueryService;
    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(PlanAuthorityValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long planId, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        try {
            Long userId = Long.valueOf(authentication.getName());
            Long writerId = calenderQueryService.findPlanWriter(planId);
            Long ownerId = moimQueryService.findMoimOwner(planId);

            if (userId.equals(writerId) || userId.equals(ownerId)) {
                return true;
            }
            addConstraintViolation(context, PLAN_EDIT_UNAUTHORIZED.toString(), "userId");
            return false;
        } catch(PlanException | MoimException e) {
            log.info("error : {}", e.getMessage());
            addConstraintViolation(context, PLAN_NOT_FOUND.toString(), "planId");
            return false;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String propertyNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(propertyNode)
                .addConstraintViolation();
    }
}