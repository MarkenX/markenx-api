package com.udla.markenx.api.classroom.courses.domain.exceptions;

public class AcademicTermNotUpcomingException extends CourseException {

    private static final String MESSAGE =
            "El periodo académico '%s' no tiene estado UPCOMING. Solo se pueden crear cursos en periodos académicos próximos";

    public AcademicTermNotUpcomingException(String academicTermId) {
        super(String.format(MESSAGE, academicTermId));
    }
}
