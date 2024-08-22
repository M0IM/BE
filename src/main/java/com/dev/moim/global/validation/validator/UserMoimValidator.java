package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.validation.annotation.MoimValidation;
import com.dev.moim.global.validation.annotation.UserMoimValidaton;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_MOIM_MEMBER;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMoimValidator implements ConstraintValidator<UserMoimValidaton, Long> {

    private final UserMoimRepository userMoimRepository;

    @Override
    public void initialize(UserMoimValidaton constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(@MoimValidation Long moimId, ConstraintValidatorContext context) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Boolean isValidMember = userMoimRepository.existsByUserIdAndMoimId(Long.valueOf(authentication.getName()), moimId);

        if (!isValidMember) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INVALID_MOIM_MEMBER.toString())
                    .addPropertyNode("userId")
                    .addPropertyNode("moimId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
