package com.udla.markenx.api.classroom.academicterms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.academicterms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

public interface CreateTermUseCase {
    TermPortDTO handle(CreateTermCommand command);
}
