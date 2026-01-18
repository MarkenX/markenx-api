package com.udla.markenx.api.classroom.terms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.ChangeTermStatusCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.commands.UpdateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQuery;

public interface UpdateTermUseCase {
    TermPortDTO update(UpdateTermCommand command);
    TermPortDTO changeStatus(ChangeTermStatusCommand command);
    TermPortDTO getById(TermIdQuery query);
}
