package com.udla.markenx.api.classroom.academicterms.application.ports.incoming;

import com.udla.markenx.api.classroom.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

public interface SaveAcademicTermUseCase {
    AcademicTerm handle(SaveAcademicTermCommand command);
}
