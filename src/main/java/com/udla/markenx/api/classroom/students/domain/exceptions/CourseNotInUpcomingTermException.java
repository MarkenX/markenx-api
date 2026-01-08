package com.udla.markenx.api.classroom.students.domain.exceptions;

public class CourseNotInUpcomingTermException extends StudentException {

    private static final String MESSAGE =
            "El curso '%s' no pertenece a un periodo académico con estado UPCOMING. " +
            "Solo se pueden asignar estudiantes a cursos en periodos académicos próximos";

    public CourseNotInUpcomingTermException(String courseId) {
        super(String.format(MESSAGE, courseId));
    }
}
