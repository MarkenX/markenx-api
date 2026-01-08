package com.udla.markenx.api.game.dimensions.application.services;

import com.udla.markenx.api.game.dimensions.application.ports.incoming.DimensionQueryUseCase;
import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.dimensions.domain.ports.outgoing.DimensionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DimensionQueryService implements DimensionQueryUseCase {

    private final DimensionQueryRepository repository;

    @Override
    public List<Dimension> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Dimension> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Dimension> getByScenarioId(String scenarioId) {
        return repository.findByScenarioId(scenarioId);
    }
}
