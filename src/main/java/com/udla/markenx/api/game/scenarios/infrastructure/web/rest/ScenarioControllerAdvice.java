package com.udla.markenx.api.game.scenarios.infrastructure.web.rest;

import com.udla.markenx.api.game.scenarios.domain.exceptions.ScenarioException;
import com.udla.markenx.api.shared.infrastructure.web.dtos.ApiErrorResponse;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = ScenarioController.class)
public class ScenarioControllerAdvice {

    private static final String SCENARIO_ERROR_CODE = "SCENARIO_ERROR";
    private static final String SCENARIO_NOT_FOUND_CODE = "SCENARIO_NOT_FOUND";

    @ExceptionHandler(ScenarioException.class)
    public ResponseEntity<ApiErrorResponse> handleScenarioException(ScenarioException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorResponse(SCENARIO_ERROR_CODE, ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiErrorResponse(SCENARIO_NOT_FOUND_CODE, ex.getMessage()));
    }
}
