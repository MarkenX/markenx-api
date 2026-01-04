package com.udla.markenx.api.assignments.domain.exceptions;

public class AssignmentException extends RuntimeException {

  protected AssignmentException(String message) {
    super(message);
  }

  protected AssignmentException(String message, Throwable cause) {
    super(message, cause);
  }
}
