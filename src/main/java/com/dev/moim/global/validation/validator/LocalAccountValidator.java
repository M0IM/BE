package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.account.dto.JoinRequest;
import com.dev.moim.domain.account.repository.UserRepository;
import com.dev.moim.global.validation.annotation.LocalAccountValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.dev.moim.domain.account.entity.enums.Provider.LOCAL;
import static com.dev.moim.global.common.code.status.ErrorStatus.EMAIL_DUPLICATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalAccountValidator implements ConstraintValidator<LocalAccountValidation, JoinRequest> {

    private final UserRepository userRepository;

    @Override
    public void initialize(LocalAccountValidation constraintAnnotation) {}

    @Override
    public boolean isValid(JoinRequest request, ConstraintValidatorContext context) {

        if (request.provider() != LOCAL) return true;

        boolean isDuplicated = userRepository.existsByEmail(request.email());

        if (isDuplicated) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(EMAIL_DUPLICATION.getMessage())
                    .addPropertyNode("email")
                    .addConstraintViolation();

            log.warn("이미 가입된 메일입니다.");
            return false;
        }
        return true;
    }
}