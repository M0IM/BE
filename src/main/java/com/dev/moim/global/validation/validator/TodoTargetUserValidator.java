package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.dto.task.CreateTodoDTO;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.global.validation.annotation.TodoTargetUserValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TodoTargetUserValidator implements ConstraintValidator<TodoTargetUserValidation, CreateTodoDTO> {

    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(TodoTargetUserValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateTodoDTO request, ConstraintValidatorContext context) {
        boolean isAssigneeSelectAll = request.isAssigneeSelectAll();
        List<Long> targetUserIdList = request.targetUserIdList();

        if (isAssigneeSelectAll && !(targetUserIdList == null || targetUserIdList.isEmpty())) {
            addConstraintViolation(context, TODO_ASSIGNEE_NOT_MATCH.getMessage());
            return false;
        }

        if (!isAssigneeSelectAll && (targetUserIdList == null || targetUserIdList.isEmpty())) {
            addConstraintViolation(context, TODO_ASSIGNEE_NULL.getMessage());
            return false;
        }

        if (!isAssigneeSelectAll) {
            List<Long> userMoimIdList = moimQueryService.findAllMemberIdByMoimId(request.moimId());
            if (!new HashSet<>(userMoimIdList).containsAll(targetUserIdList)) {
                addConstraintViolation(context, INVALID_MOIM_MEMBER.getMessage());
                return false;
            }
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("targetUserIdList")
                .addConstraintViolation();
    }
}
