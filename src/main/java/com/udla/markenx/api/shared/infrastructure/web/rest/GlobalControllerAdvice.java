package com.udla.markenx.api.shared.infrastructure.web.rest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.udla.markenx.api.shared.domain.exceptions.EntityException;
import com.udla.markenx.api.shared.infrastructure.web.dtos.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
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
    public ResponseEntity<ApiErrorResponse> handleValidation(
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ----------------------------
    // 400 - Invalid / malformed JSON
    // ----------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleUnreadableBody(
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // ----------------------------
    // 409 - Conflicts
    // ----------------------------
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(
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

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    // ----------------------------
    // 422 - Domain / business rule violations
    // ----------------------------
    @ExceptionHandler(EntityException.class)
    public ResponseEntity<ApiErrorResponse> handleDomain(
            @NonNull EntityException ex,
            @NonNull HttpServletRequest request
    ) {
        HttpStatus status = ex.status() == null ? HttpStatus.valueOf(422) : ex.status();
        String code = ex.code() == null ? "DOMAIN_ERROR" : ex.code();

        var body = ApiErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code(code)
                .message("Domain rule violated")
                .userMessage(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }

    // ----------------------------
    // Spring "typed" errors
    // ----------------------------
    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ApiErrorResponse> handleSpringErrorResponse(
            @NonNull ErrorResponseException ex,
            @NonNull HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());

        ex.getMessage();
        var body = ApiErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(status.value())
                .error(status.getReasonPhrase())
                .code("HTTP_ERROR")
                .message(ex.getMessage())
                .userMessage("Ocurrió un error inesperado. Inténtalo más tarde.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(status).body(body);
    }

    // ----------------------------
    // 500 - Fallback
    // ----------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnhandled(
            @NonNull Exception ex,
            @NonNull HttpServletRequest request
    ) {
        var body = ApiErrorResponse.builder()
                .timestamp(Instant.now().toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .code("INTERNAL_ERROR")
                .message("Unexpected error")
                .userMessage("Ocurrió un error inesperado. Inténtalo más tarde.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
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
