package com.udla.markenx.api.classroom.students.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntitiesNotFoundException;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

public abstract class StudentException extends RuntimeException {

    private static final String CODE = "STUDENT_EXCEPTION";
    private static final String ENTITY_NAME = "Estudiante";
    private static final String EMAIL_CRITERIA = "correo";

    protected StudentException(String message) {
        super(message);
    }

    protected StudentException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(CODE, ENTITY_NAME, id);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundByEmail(String email) {
        return EntityNotFoundException.byCriteria(CODE, ENTITY_NAME, EMAIL_CRITERIA, email);
    }

    @Contract(" -> new")
    public static @NonNull EntitiesNotFoundException noneFound() {
        return EntitiesNotFoundException.none(CODE, ENTITY_NAME);
    }
}
