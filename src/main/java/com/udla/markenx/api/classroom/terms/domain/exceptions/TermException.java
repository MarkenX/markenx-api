package com.udla.markenx.api.classroom.terms.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntitiesNotFoundException;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import com.udla.markenx.api.shared.domain.exceptions.EntityException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public abstract class TermException extends EntityException {

    private static final String CODE = "TERM_EXCEPTION";
    private static final String ENTITY_NAME = "Periodo académico";
    private static final String STATUS_CRITERIA = "estatus";
    private static final String LIFECYCLE_STATUS_CRITERIA = "estado de vida";

    protected TermException(String message) {
        super(CODE, message);
    }

    @Contract("_ -> new")
    public static @NonNull EntityNotFoundException notFoundById(String id) {
        return EntityNotFoundException.byId(CODE, ENTITY_NAME, id);
    }

    @Contract(" -> new")
    public static @NonNull EntitiesNotFoundException noneFound() {
        return EntitiesNotFoundException.none(CODE, ENTITY_NAME);
    }

    public static @NonNull EntitiesNotFoundException noneFoundByLifecycleStatus(String status) {
        return EntitiesNotFoundException.byCriteria(CODE, ENTITY_NAME, LIFECYCLE_STATUS_CRITERIA, status);
    }

    @Contract("_ -> new")
    public static @NonNull EntitiesNotFoundException noneFoundByStatuses(Set<String> statuses) {
        return EntitiesNotFoundException.byCriteria(CODE, ENTITY_NAME, STATUS_CRITERIA, statuses);
    }
}