package com.udla.markenx.api.classroom.courses.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntitiesNotFoundException;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class CourseException extends RuntimeException {

    private static final String ENTITY_NAME = "Curso";
    private static final String STATUS_CRITERIA = "estatus";

    public CourseException(String message) {
        super(message);
    }

    public CourseException(String message, Throwable cause) {
        super(message, cause);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(ENTITY_NAME, id);
    }

    @Contract(" -> new")
    public static @NonNull EntitiesNotFoundException noneFound() {
        return EntitiesNotFoundException.none(ENTITY_NAME);
    }

    @Contract("_ -> new")
    public static @NonNull EntitiesNotFoundException noneFoundByStatuses(Set<String> statuses) {
        return EntitiesNotFoundException.byCriteria(ENTITY_NAME, STATUS_CRITERIA, statuses);
    }
}
