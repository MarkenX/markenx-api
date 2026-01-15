package com.udla.markenx.api.security.infrastructure.listeners;

import com.udla.markenx.api.security.domain.events.UserIdentityRollbackEvent;
import com.udla.markenx.api.security.domain.ports.outgoing.UserCommandRepository;
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
