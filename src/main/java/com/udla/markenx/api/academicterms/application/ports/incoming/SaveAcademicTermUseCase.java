package com.udla.markenx.api.academicterms.application.ports.incoming;

import com.udla.markenx.api.academicterms.application.commands.SaveAcademicTermCommand;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;

public interface SaveAcademicTermUseCase {
    AcademicTerm handle(SaveAcademicTermCommand command);
}
