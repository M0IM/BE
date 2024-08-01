package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.validation.annotation.OAuthAccountValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.domain.account.entity.enums.Provider.LOCAL;
import static com.dev.moim.global.common.code.status.ErrorStatus.OAUTH_ACCOUNT_DUPLICATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAccountValidator implements ConstraintValidator<OAuthAccountValidation, JoinRequest> {

    private final UserRepository userRepository;

    @Override
    public void initialize(OAuthAccountValidation constraintAnnotation) {}

    @Override
    public boolean isValid(JoinRequest request, ConstraintValidatorContext context) {

        if (request.provider() == LOCAL) return true;

        boolean isDuplicated = userRepository.existsByProviderAndProviderId(request.provider(), request.providerId());

        if (isDuplicated) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(OAUTH_ACCOUNT_DUPLICATION.getMessage())
                    .addPropertyNode("providerId")
                    .addConstraintViolation();

            log.warn("이미 가입된 소셜 계정입니다.");
            return false;
        }
        return true;
    }
}
