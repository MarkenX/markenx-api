package com.udla.markenx.api.shared.infrastructure.web.rest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.udla.markenx.api.shared.infrastructure.web.dtos.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    // ----------------------------
    // 400 - Bean Validation (@Valid)
    // ----------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleValidation(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpServletRequest request
    ) {
        List<ApiErrorResponse.FieldViolation> violations = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toViolation)
                .toList();

        String message = violations.isEmpty()
                ? "Validation error"
                : ("Validation error: " + violations.getFirst().field() + " " + violations.getFirst().message());

        var body = ApiErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("VALIDATION_ERROR")
                .message(message)
                .userMessage("Revisa los datos ingresados e inténtalo nuevamente.")
                .path(request.getRequestURI())
                .violations(violations)
                .build();

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ----------------------------
    // 400 - Invalid / malformed JSON
    // ----------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleUnreadableBody(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpServletRequest request
    ) {
        String message = "Request body is invalid or malformed";

        // Optional: give a better hint when the JSON contains an invalid enum/date/number
        Throwable root = rootCause(ex);
        if (root instanceof InvalidFormatException ife) {
            String field = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("."));
            if (!field.isBlank()) {
                message = "Invalid value for field: " + field;
            }
        }

        var body = ApiErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .code("INVALID_JSON")
                .message(message)
                .userMessage("El formato de la solicitud no es válido.")
                .path(request.getRequestURI())
                .build();

        return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ----------------------------
    // 409 - Conflicts
    // ----------------------------
    @ExceptionHandler(IllegalStateException.class)
    public org.springframework.http.ResponseEntity<ApiErrorResponse> handleConflict(
            @NonNull IllegalStateException ex,
            @NonNull HttpServletRequest request
    ) {
        var body = ApiErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .code("CONFLICT")
                .message(ex.getMessage() == null ? "Conflict" : ex.getMessage())
                .userMessage("Ya existe un registro con esos datos.")
                .path(request.getRequestURI())
                .build();

        return org.springframework.http.ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @Contract("_ -> new")
    private ApiErrorResponse.@NonNull FieldViolation toViolation(@NonNull FieldError fe) {
        return new ApiErrorResponse.FieldViolation(
                fe.getField(),
                fe.getDefaultMessage() == null ? "is invalid" : fe.getDefaultMessage(),
                fe.getRejectedValue()
        );
    }

    private static Throwable rootCause(Throwable ex) {
        Throwable cur = ex;
        while (cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur;
    }
}
