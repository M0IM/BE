package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.global.validation.annotation.MoimMemberListValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.INVALID_MOIM_MEMBER;

@Slf4j
@Component
@RequiredArgsConstructor
public class MoimMemberListValidator implements ConstraintValidator<MoimMemberListValidation, CreateTodoDTO> {

    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(MoimMemberListValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateTodoDTO request, ConstraintValidatorContext context) {

        List<Long> userMoimIdList = moimQueryService.findAllUserIdByMoimId(request.moimId());

        boolean AreValidMembers = new HashSet<>(userMoimIdList).containsAll(request.targetUserIdList());

        if (!AreValidMembers) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(INVALID_MOIM_MEMBER.getMessage())
                    .addPropertyNode("targetUserIdList")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
