package com.udla.markenx.api.academicterms.application.ports.incoming;

import com.udla.markenx.api.academicterms.application.commands.ChangeAcademicTermStatusCommand;
import com.udla.markenx.api.academicterms.application.commands.UpdateAcademicTermCommand;
import com.udla.markenx.api.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;

public interface UpdateAcademicTermUseCase {
    AcademicTerm update(UpdateAcademicTermCommand command);
    AcademicTerm changeStatus(ChangeAcademicTermStatusCommand command);
    AcademicTerm getById(GetAcademicTermByIdQuery query);
}
