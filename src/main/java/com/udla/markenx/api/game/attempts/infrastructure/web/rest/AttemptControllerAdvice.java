package com.udla.markenx.api.game.attempts.infrastructure.web.rest;

import com.udla.markenx.api.game.attempts.domain.exceptions.AttemptException;
import com.udla.markenx.api.game.attempts.domain.exceptions.AttemptNotFoundException;
import com.udla.markenx.api.shared.infrastructure.web.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.udla.markenx.api.game.attempts")
public class AttemptControllerAdvice {

    private static final String ATTEMPT_ERROR_CODE = "ATTEMPT_ERROR";
    private static final String ATTEMPT_NOT_FOUND_CODE = "ATTEMPT_NOT_FOUND";

    @ExceptionHandler(AttemptNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleAttemptNotFoundException(AttemptNotFoundException ex) {
        return new ErrorResponse(ATTEMPT_NOT_FOUND_CODE, ex.getMessage());
    }

    @ExceptionHandler(AttemptException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDomainException(AttemptException ex) {
        return new ErrorResponse(ATTEMPT_ERROR_CODE, ex.getMessage());
    }
}
