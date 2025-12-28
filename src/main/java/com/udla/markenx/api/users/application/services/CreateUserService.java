package com.udla.markenx.api.users.application.services;

import com.udla.markenx.api.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.users.application.ports.incoming.CreateUserUseCase;
import com.udla.markenx.api.users.domain.models.aggregates.User;
import com.udla.markenx.api.users.domain.models.valueobjects.Email;
import com.udla.markenx.api.users.domain.ports.outgoing.UserCommandRepository;
import com.udla.markenx.api.users.domain.ports.outgoing.UserIdentityProvider;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserUseCase {

    private final UserCommandRepository repository;
    private final UserIdentityProvider identityProvider;

    @Override
    public void handle(@NonNull CreateUserCommand command) {
        var email = Email.of(command.email());
        User newUser = User.create(email, command.role());

        User saved = repository.save(newUser);
        identityProvider.createExternalIdentity(saved.getEmail());
    }
}
