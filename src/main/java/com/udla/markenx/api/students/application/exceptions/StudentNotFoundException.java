package com.udla.markenx.api.students.application.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;

public class StudentNotFoundException extends EntityNotFoundException {
    public StudentNotFoundException(String studentId) {
        super("No se encontró el curso con el identificador: " + studentId);
    }
}
