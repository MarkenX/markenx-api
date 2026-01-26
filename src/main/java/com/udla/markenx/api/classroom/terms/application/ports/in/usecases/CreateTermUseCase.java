package com.udla.markenx.api.classroom.terms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.terms.application.ports.in.commands.CreateTermCommand;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;

public interface CreateTermUseCase {
    TermPortDTO handle(CreateTermCommand command);
}
