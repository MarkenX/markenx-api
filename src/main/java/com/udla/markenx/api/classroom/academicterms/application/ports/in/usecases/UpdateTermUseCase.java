package com.udla.markenx.api.classroom.academicterms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.academicterms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.queries.TermIdQueryCriteria;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

public interface UpdateTermUseCase {
    TermPortDTO update(UpdateTermCommand command);
    TermPortDTO changeStatus(ChangeTermStatusCommand command);
    TermPortDTO getById(TermIdQueryCriteria query);
}
