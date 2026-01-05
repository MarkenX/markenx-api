package com.udla.markenx.api.classroom.academicterms.infrastructure.web;

import com.udla.markenx.api.classroom.academicterms.domain.exceptions.AcademicTermException;
import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.academicterms")
public class AcademicTermControllerAdvice {

    @ExceptionHandler(AcademicTermException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDomainException(AcademicTermException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
