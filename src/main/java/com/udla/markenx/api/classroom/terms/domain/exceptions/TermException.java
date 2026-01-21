package com.udla.markenx.api.classroom.terms.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntitiesNotFoundException;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public abstract class TermException extends RuntimeException {

    private static final String ENTITY_NAME = "Periodo académico";
    private static final String YEAR_CRITERIA = "año";
    private static final String STATUS_CRITERIA = "estatus";

    protected TermException(String message) {
        super(message);
    }

    protected TermException(String message, Throwable cause) {
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
    public static @NonNull EntitiesNotFoundException noneFoundByYear(int year) {
        return EntitiesNotFoundException.byCriteria(ENTITY_NAME, YEAR_CRITERIA, year);
    }

    @Contract("_ -> new")
    public static @NonNull EntitiesNotFoundException noneFoundByStatuses(Set<String> statuses) {
        return EntitiesNotFoundException.byCriteria(ENTITY_NAME, STATUS_CRITERIA, statuses);
    }
}