package com.dev.moim.global.validation.validator;

import com.dev.moim.domain.moim.dto.task.DeleteTodoAssigneeDTO;
import com.dev.moim.domain.moim.service.MoimQueryService;
import com.dev.moim.domain.moim.service.TodoQueryService;
import com.dev.moim.global.validation.annotation.DeleteAssigneeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.dev.moim.global.common.code.status.ErrorStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteAssigneeValidator implements ConstraintValidator<DeleteAssigneeValidation, DeleteTodoAssigneeDTO> {

    private final TodoQueryService todoQueryService;
    private final MoimQueryService moimQueryService;

    @Override
    public void initialize(DeleteAssigneeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(DeleteTodoAssigneeDTO request, ConstraintValidatorContext context) {

        List<Long> deleteAssigneeIdList = request.deleteAssigneeIdList();
        List<Long> moimMemberIdList = moimQueryService.findAllMemberIdByMoimId(request.moimId());

        boolean hasNonMember = deleteAssigneeIdList.stream()
                .anyMatch(id -> !moimMemberIdList.contains(id));
        if (hasNonMember) {
            addConstraintViolation(context, INVALID_MOIM_MEMBER.getMessage());
            return false;
        }

        List<Long> assigneeIdList = todoQueryService.findAssigneeByTodoId(request.todoId()).stream()
                .map(userTodo -> userTodo.getUser().getId())
                .toList();

        boolean hasNonAssignee = deleteAssigneeIdList.stream()
                .anyMatch(id -> !assigneeIdList.contains(id));
        if (hasNonAssignee) {
            addConstraintViolation(context, NOT_TODO_ASSIGNEE.getMessage());
            return false;
        }
        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode("deleteAssigneeIdList")
                .addConstraintViolation();
    }
}
