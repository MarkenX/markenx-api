package com.udla.markenx.api.classroom.assignments.domain.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntitiesNotFoundException;
import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.util.Set;

public class AssignmentException extends RuntimeException {

  private static final String CODE = "ASSIGNMENT_EXCEPTION";
  private static final String ENTITY_NAME = "Asignación";
  private static final String STATUS_CRITERIA = "estatus";

  protected AssignmentException(String message) {
    super(message);
  }

  protected AssignmentException(String message, Throwable cause) {
    super(message, cause);
  }

  @Contract("_ -> new")
  public static @NonNull EntityNotFoundException notFoundById(String id) {
    return EntityNotFoundException.byId(CODE, ENTITY_NAME, id);
  }

  @Contract(" -> new")
  public static @NonNull EntitiesNotFoundException noneFound() {
    return EntitiesNotFoundException.none(CODE, ENTITY_NAME);
  }

  @Contract("_ -> new")
  public static @NonNull EntitiesNotFoundException noneFoundByStatuses(Set<String> statuses) {
    return EntitiesNotFoundException.byCriteria(CODE, ENTITY_NAME, STATUS_CRITERIA, statuses);
  }
}
