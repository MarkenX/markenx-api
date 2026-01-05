package com.udla.markenx.api.classroom.academicterms.application.exceptions;

import com.udla.markenx.api.shared.application.exceptions.EntityNotFoundException;

public class AcademicTermNotFoundException extends EntityNotFoundException {
    public AcademicTermNotFoundException(String termId) {
        super("No se encontró el período académico con el identificador: " + termId);
    }
}
