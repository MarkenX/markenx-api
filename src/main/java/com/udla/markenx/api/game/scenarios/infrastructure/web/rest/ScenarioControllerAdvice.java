package com.udla.markenx.api.game.scenarios.infrastructure.web.rest;

import com.udla.markenx.api.game.scenarios.domain.exceptions.ScenarioException;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice(assignableTypes = ScenarioController.class)
public class ScenarioControllerAdvice {

    @ExceptionHandler(ScenarioException.class)
    public ResponseEntity<Map<String, String>> handleScenarioException(ScenarioException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }
}
