package com.udla.markenx.api.classroom.assignments.domain.exceptions;

public class CourseNotInUpcomingTermException extends AssignmentException {

    private static final String MESSAGE =
            "El curso '%s' no pertenece a un periodo académico con estado UPCOMING. " +
            "Solo se pueden crear tareas en cursos con periodos académicos próximos";

    public CourseNotInUpcomingTermException(String courseId) {
        super(String.format(MESSAGE, courseId));
    }
}
