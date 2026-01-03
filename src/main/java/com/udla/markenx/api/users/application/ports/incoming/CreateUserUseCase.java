package com.udla.markenx.api.users.application.ports.incoming;

import com.udla.markenx.api.users.application.commands.CreateUserCommand;

public interface CreateUserUseCase {
    String handle(CreateUserCommand command);
}
