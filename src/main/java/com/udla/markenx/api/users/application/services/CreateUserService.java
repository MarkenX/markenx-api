package com.udla.markenx.api.users.application.services;

import com.udla.markenx.api.users.application.commands.CreateUserCommand;
import com.udla.markenx.api.users.application.ports.incoming.CreateUserUseCase;
import com.udla.markenx.api.users.domain.models.aggregates.User;
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
    public void execute(@NonNull CreateUserCommand command) {
        User user = repository.findById(command.UserId());
        identityProvider.createExternalIdentity(user.getEmail());
    }
}
