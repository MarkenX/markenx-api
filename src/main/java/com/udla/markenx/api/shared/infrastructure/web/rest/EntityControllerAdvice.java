package com.udla.markenx.api.shared.infrastructure.web.rest;

import com.udla.markenx.api.shared.infrastructure.web.dtos.ErrorResponse;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EntityControllerAdvice {

    private static final String ENTITY_ERROR_CODE = "ENTITY_NOT_FOUND";

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleDomainException(@NonNull EntityNotFoundException ex) {
        return new ErrorResponse(ENTITY_ERROR_CODE, ex.getMessage());
    }
}
