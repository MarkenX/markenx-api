package com.udla.markenx.api.users.application.ports.incoming;

import com.udla.markenx.api.users.application.commands.CreateUserCommand;

public interface CreateUserUseCase {
    void execute(CreateUserCommand command);
}
