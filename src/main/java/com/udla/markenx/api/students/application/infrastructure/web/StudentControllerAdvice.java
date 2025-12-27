package com.udla.markenx.api.students.application.infrastructure.web;

import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import com.udla.markenx.api.students.domain.exceptions.StudentException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.students")
public class StudentControllerAdvice {

    @ExceptionHandler(StudentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(StudentException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
