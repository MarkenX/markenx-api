package com.udla.markenx.api.shared.infrastructure.web.dtos;

import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.List;


@SuppressWarnings("LombokGetterMayBeUsed")
public class ApiErrorResponse {
    private final String timestamp;
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String userMessage;
    private final String path;
    private final List<FieldViolation> violations;

    private ApiErrorResponse(@NonNull Builder b) {
        this.timestamp = b.timestamp;
        this.status = b.status;
        this.error = b.error;
        this.code = b.code;
        this.message = b.message;
        this.userMessage = b.userMessage;
        this.path = b.path;
        this.violations = b.violations;
    }

    public String getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getUserMessage() { return userMessage; }
    public String getPath() { return path; }
    public List<FieldViolation> getViolations() { return violations; }

    @Contract(value = " -> new", pure = true)
    public static @NonNull Builder builder() { return new Builder(); }

    public record FieldViolation(String field, String message, Object rejectedValue) {}

    public static final class Builder {
        private String timestamp;
        private int status;
        private String error;
        private String code;
        private String message;
        private String userMessage;
        private String path;
        private List<FieldViolation> violations;

        public Builder timestamp(String v) { this.timestamp = v; return this; }
        public Builder status(int v) { this.status = v; return this; }
        public Builder error(String v) { this.error = v; return this; }
        public Builder code(String v) { this.code = v; return this; }
        public Builder message(String v) { this.message = v; return this; }
        public Builder userMessage(String v) { this.userMessage = v; return this; }
        public Builder path(String v) { this.path = v; return this; }
        public Builder violations(List<FieldViolation> v) { this.violations = v; return this; }

        @Contract(" -> new")
        public @NonNull ApiErrorResponse build() { return new ApiErrorResponse(this); }
    }
}
