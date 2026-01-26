package com.udla.markenx.api.security.application.ports.in.usecases;

import com.udla.markenx.api.security.application.ports.in.commands.CreateUserCommand;

public interface CreateUserUseCase {
    String handle(CreateUserCommand command);
}
