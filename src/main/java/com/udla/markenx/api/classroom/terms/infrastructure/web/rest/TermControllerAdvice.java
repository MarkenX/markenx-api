package com.udla.markenx.api.classroom.terms.infrastructure.web.rest;

import com.udla.markenx.api.classroom.terms.domain.exceptions.TermException;
import com.udla.markenx.api.shared.infrastructure.web.dtos.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.classroom.academicterms")
public class TermControllerAdvice {

    private static final String ACADEMIC_TERM_ERROR_CODE = "ACADEMIC_TERM_ERROR";

    @ExceptionHandler(TermException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiErrorResponse handleDomainException(TermException ex) {
        return new ApiErrorResponse(ACADEMIC_TERM_ERROR_CODE, ex.getMessage());
    }
}
