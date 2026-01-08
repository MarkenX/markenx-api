package com.udla.markenx.api.classroom.courses.infrastructure.web;

import com.udla.markenx.api.classroom.courses.domain.exceptions.CourseException;
import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.courses")
public class CourseControllerAdvice {

    @ExceptionHandler(CourseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(@NonNull CourseException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
