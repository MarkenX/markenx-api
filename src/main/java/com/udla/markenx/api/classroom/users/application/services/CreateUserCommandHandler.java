package com.udla.markenx.api.classroom.users.application.services;

import com.udla.markenx.api.classroom.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.classroom.users.application.ports.incoming.CreateUserUseCase;
import com.udla.markenx.api.classroom.users.application.ports.outgoing.ExternalIdentityPort;
import com.udla.markenx.api.classroom.users.domain.models.aggregates.User;
import com.udla.markenx.api.classroom.users.domain.models.valueobjects.Email;
import com.udla.markenx.api.classroom.users.domain.models.valueobjects.Role;
import com.udla.markenx.api.classroom.users.domain.ports.outgoing.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserCommandHandler implements CreateUserUseCase {

    private final UserCommandRepository repository;
    private final ExternalIdentityPort identityProvider;

    @Override
    public String handle(@NonNull CreateUserCommand command) {
        var email = Email.of(command.email());
        User newUser = User.create(email, Role.valueOf(command.role()));

        User saved = repository.save(newUser);
        identityProvider.createIdentity(saved.getEmail());
        return saved.getId();
    }
}
