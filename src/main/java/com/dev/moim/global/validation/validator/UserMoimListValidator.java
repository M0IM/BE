package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.UserMoimListValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_MOIM_MEMBER;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMoimListValidator implements ConstraintValidator<UserMoimListValidation, List<Long>> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(UserMoimListValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<Long> moimIdList, ConstraintValidatorContext context) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (moimIdList == null || moimIdList.isEmpty()) {
            return true;
        }

        List<Long> userMoimIdList = userQueryService.findUserMoimIdListByUserId(Long.valueOf(authentication.getName()));

        boolean isValidMember = new HashSet<>(userMoimIdList).containsAll(moimIdList);

        if (!isValidMember) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INVALID_MOIM_MEMBER.getMessage())
                    .addPropertyNode("moimId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
