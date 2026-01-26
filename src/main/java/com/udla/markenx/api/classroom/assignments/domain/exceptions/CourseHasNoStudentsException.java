package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class CourseHasNoStudentsException extends AssignmentException {

    private static final String MESSAGE =
            "El curso '%s' no tiene estudiantes matriculados. " +
            "No se pueden crear tareas en cursos sin estudiantes";

    public CourseHasNoStudentsException(String courseId) {
        super(String.format(MESSAGE, courseId));
    }
}
