package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.ProfileOwnerValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileOwnerValidator implements ConstraintValidator<ProfileOwnerValidation, Long>  {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(ProfileOwnerValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long profileId, ConstraintValidatorContext context) {

        Optional<UserProfile> userProfile = userQueryService.findUserProfile(profileId);

        if (userProfile.isEmpty()) {
            addConstraintViolation(context, USER_PROFILE_NOT_FOUND.getMessage());
            return false;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!userProfile.get().getUser().getId().equals(Long.valueOf(authentication.getName()))) {
            addConstraintViolation(context, NOT_USER_PROFILE_OWNER.getMessage());
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
