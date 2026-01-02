package com.udla.markenx.api.users.infrastructure.eventlisteners;

import com.udla.markenx.api.users.domain.events.UserIdentityRollbackEvent;
import com.udla.markenx.api.users.domain.ports.outgoing.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRollbackListener {

    private final UserCommandRepository repository;

    @EventListener
    public void on(UserIdentityRollbackEvent event) {
        repository.deleteById(event.userId());
    }
}
