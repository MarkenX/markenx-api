package com.udla.markenx.api.shared.application.dtos;

import java.util.Map;

/**
 * Standard error response for the API.
 *
 * @param code Module-specific error code (e.g., STUDENT_ERROR, COURSE_ERROR)
 * @param message Human-readable error message
 * @param details Optional additional context about the error
 */
public record ErrorResponse(
        String code,
        String message,
        Map<String, Object> details
) {
    /**
     * Creates an ErrorResponse without details.
     */
    public ErrorResponse(String code, String message) {
        this(code, message, null);
    }
}
