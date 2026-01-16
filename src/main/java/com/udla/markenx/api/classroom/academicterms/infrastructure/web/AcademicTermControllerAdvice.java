package com.udla.markenx.api.classroom.academicterms.infrastructure.web;

import com.udla.markenx.api.classroom.academicterms.domain.exceptions.AcademicTermException;
import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.classroom.academicterms")
public class AcademicTermControllerAdvice {

    private static final String ACADEMIC_TERM_ERROR_CODE = "ACADEMIC_TERM_ERROR";

    @ExceptionHandler(AcademicTermException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDomainException(AcademicTermException ex) {
        return new ErrorResponse(ACADEMIC_TERM_ERROR_CODE, ex.getMessage());
    }
}
