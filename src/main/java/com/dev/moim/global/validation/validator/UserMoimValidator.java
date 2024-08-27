package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.global.validation.annotation.UserMoimValidaton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.domain.moim.entity.enums.JoinStatus.COMPLETE;
import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_MOIM_MEMBER;
import static com.dev.moim.global.common.code.status.ErrorStatus.MOIM_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMoimValidator implements ConstraintValidator<UserMoimValidaton, Long> {

    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(UserMoimValidaton constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long moimId, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isValidMoim = moimQueryService.existsByMoimId(moimId);
        if (!isValidMoim) {
            addConstraintViolation(context, MOIM_NOT_FOUND.toString(), "moimId");
            return false;
        }

        boolean isValidMember = moimQueryService.existsByUserIdAndMoimIdAndJoinStatus(Long.valueOf(authentication.getName()), moimId, COMPLETE);
        if (!isValidMember) {
            addConstraintViolation(context, INVALID_MOIM_MEMBER.toString(), "userId");
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
