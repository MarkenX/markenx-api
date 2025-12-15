package com.udla.markenx.api.academicterms.application.ports.incoming;

import com.udla.markenx.api.academicterms.application.commands.ChangeAcademicTermStatusCommand;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;

public interface UpdateAcademicTermUseCase {
    AcademicTerm disable(ChangeAcademicTermStatusCommand command);
}
