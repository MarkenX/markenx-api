package com.udla.markenx.api.shared.infrastructure.web;

import com.udla.markenx.api.shared.application.dtos.ErrorResponse;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice()
public class EntityControllerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDomainException(EntityNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
