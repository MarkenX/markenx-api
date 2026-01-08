package com.udla.markenx.api.game.consumers.application.services;

import com.udla.markenx.api.game.consumers.application.ports.incoming.ConsumerQueryUseCase;
import com.udla.markenx.api.game.consumers.domain.models.aggregates.Consumer;
import com.udla.markenx.api.game.consumers.domain.ports.outgoing.ConsumerQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsumerQueryService implements ConsumerQueryUseCase {

    private final ConsumerQueryRepository repository;

    @Override
    public List<Consumer> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Consumer> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Consumer> getByScenarioId(String scenarioId) {
        return repository.findByScenarioId(scenarioId);
    }
}
