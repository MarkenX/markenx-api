package com.udla.markenx.api.classroom.academicterms.application.ports.incoming;

import com.udla.markenx.api.classroom.academicterms.application.commands.ChangeAcademicTermStatusCommand;
import com.udla.markenx.api.classroom.academicterms.application.commands.UpdateAcademicTermCommand;
import com.udla.markenx.api.classroom.academicterms.application.queries.GetAcademicTermByIdQuery;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

public interface UpdateAcademicTermUseCase {
    AcademicTerm update(UpdateAcademicTermCommand command);
    AcademicTerm changeStatus(ChangeAcademicTermStatusCommand command);
    AcademicTerm getById(GetAcademicTermByIdQuery query);
}
