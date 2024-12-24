package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.service.CalenderQueryService;
import com.dev.moim.global.error.handler.MoimException;
import com.dev.moim.global.error.handler.PlanException;
import com.dev.moim.global.security.principal.MoimAuthentication;
import com.dev.moim.global.validation.annotation.PlanAuthorityValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PlanAuthorityValidator implements ConstraintValidator<PlanAuthorityValidation, Long> {

    private final CalenderQueryService calenderQueryService;

    @Override
    public void initialize(PlanAuthorityValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long planId, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof MoimAuthentication moimAuth)) {
            addConstraintViolation(context, AUTHENTICATION_FAILED.getMessage());
            return false;
        }

        try {
            Long userMoimId = moimAuth.userMoimId();
            Long writerUserMoimId = calenderQueryService.findPlanWriter(planId);

            if (userMoimId.equals(writerUserMoimId) || moimAuth.moimRole().equals(MoimRole.OWNER)) {
                return true;
            }
            addConstraintViolation(context, PLAN_EDIT_UNAUTHORIZED.getMessage());
            return false;
        } catch(PlanException e) {
            addConstraintViolation(context, PLAN_NOT_FOUND.getMessage());
            return false;
        } catch (MoimException e) {
            addConstraintViolation(context, MOIM_OWNER_NOT_FOUND.getMessage());
            return false;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
