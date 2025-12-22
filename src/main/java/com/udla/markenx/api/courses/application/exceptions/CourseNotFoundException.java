package com.udla.markenx.api.courses.application.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;

public class CourseNotFoundException extends EntityNotFoundException {
    public CourseNotFoundException(String courseId) {
        super("No se encontró el curso con el identificador: " + courseId);
    }
}
