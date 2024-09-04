package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.entity.User;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.error.handler.UserException;
import com.dev.moim.global.validation.annotation.SelfReviewValidation;
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
public class SelfReviewValidator implements ConstraintValidator<SelfReviewValidation, Long> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(SelfReviewValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long targetUserId, ConstraintValidatorContext context) {

        try {
           User targetUser = userQueryService.findUserById(targetUserId)
                   .orElseThrow(() -> new UserException(USER_NOT_FOUND));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (targetUser.getId().toString().equals(authentication.getName())) {
                addConstraintViolation(context, SELF_REVIEW_FORBIDDEN.getMessage());
                return false;
            }
            return true;
        } catch (UserException e) {
            addConstraintViolation(context, USER_NOT_FOUND.getMessage());
            return false;
        }
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}