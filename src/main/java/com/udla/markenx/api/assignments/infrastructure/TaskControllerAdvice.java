package com.udla.markenx.api.assignments.infrastructure;

import com.udla.markenx.api.assignments.domain.exceptions.AssignmentException;
import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.assignments")
public class TaskControllerAdvice {

    @ExceptionHandler(AssignmentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(@NonNull AssignmentException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
