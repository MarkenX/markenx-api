package com.udla.markenx.api.classroom.terms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQueryCriteria;

public interface UpdateTermUseCase {
    TermPortDTO update(UpdateTermCommand command);
    TermPortDTO changeStatus(ChangeTermStatusCommand command);
    TermPortDTO getById(TermIdQueryCriteria query);
}
