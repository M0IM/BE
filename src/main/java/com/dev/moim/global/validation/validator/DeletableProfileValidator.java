package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.entity.UserProfile;
import com.dev.moim.domain.account.entity.enums.ProfileType;
import com.dev.moim.domain.user.service.UserQueryService;
import com.dev.moim.global.validation.annotation.DeletableProfileValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@RequiredArgsConstructor
@Component
public class DeletableProfileValidator implements ConstraintValidator<DeletableProfileValidation, Long> {

    private final UserQueryService userQueryService;

    @Override
    public void initialize(DeletableProfileValidation constraintAnnotation) {
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

        if (userProfile.get().getProfileType().equals(ProfileType.MAIN)) {
            addConstraintViolation(context, CANNOT_DELETE_MAIN_USER_PROFILE.getMessage());
            return false;
        }

        boolean isUsedProfile = userQueryService.existsByUserProfileIdAndJoinStatus(profileId);
        if (isUsedProfile) {
            addConstraintViolation(context, USER_PROFILE_IN_USE.getMessage());
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
