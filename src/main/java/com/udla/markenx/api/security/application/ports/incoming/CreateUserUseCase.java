package com.udla.markenx.api.security.application.ports.incoming;

import com.udla.markenx.api.security.application.commands.CreateUserCommand;

public interface CreateUserUseCase {
    String handle(CreateUserCommand command);
}
