package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.entity.UserMoim;
import com.dev.moim.domain.moim.entity.enums.MoimRole;
import com.dev.moim.domain.moim.repository.UserMoimRepository;
import com.dev.moim.global.validation.annotation.CheckAdminValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_MOIM_MEMBER;
import static com.dev.moim.global.common.code.status.ErrorStatus.MOIM_NOT_ADMIN;

@RequiredArgsConstructor
@Component
public class CheckAdminValidator implements ConstraintValidator<CheckAdminValidation, Long> {

    private final UserMoimRepository userMoimRepository;

    @Override
    public void initialize(CheckAdminValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long moimId, ConstraintValidatorContext context) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserMoim> userMoim = userMoimRepository.findByUserIdAndMoimId(Long.valueOf(authentication.getName()), moimId);

        if (userMoim.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INVALID_MOIM_MEMBER.toString())
                    .addPropertyNode("userId")
                    .addPropertyNode("moimId")
                    .addConstraintViolation();
            return false;
        } else {
            if (userMoim.get().getMoimRole().equals(MoimRole.ADMIN) || userMoim.get().getMoimRole().equals(MoimRole.OWNER)) {
                return true;
            } else {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(MOIM_NOT_ADMIN.toString())
                        .addPropertyNode("userId")
                        .addPropertyNode("moimId")
                        .addConstraintViolation();
                return false;
            }
        }
    }
}