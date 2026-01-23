package com.udla.markenx.api.classroom.assignments.infrastructure.web.rest;

import com.udla.markenx.api.classroom.assignments.domain.exceptions.AssignmentException;
import com.udla.markenx.api.shared.infrastructure.web.dtos.ApiErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.classroom.assignments")
public class TaskControllerAdvice {

    private static final String TASK_ERROR_CODE = "TASK_ERROR";

    @ExceptionHandler(AssignmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handleDomainException(@NonNull AssignmentException ex) {
        return new ApiErrorResponse(TASK_ERROR_CODE, ex.getMessage());
    }
}
