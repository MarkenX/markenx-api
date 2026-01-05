package com.udla.markenx.api.classroom.users.application.ports.incoming;

import com.udla.markenx.api.classroom.users.application.commands.CreateUserCommand;

public interface CreateUserUseCase {
    String handle(CreateUserCommand command);
}
