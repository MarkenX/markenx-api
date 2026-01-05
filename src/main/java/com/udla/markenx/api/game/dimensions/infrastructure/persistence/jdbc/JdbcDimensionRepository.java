package com.udla.markenx.api.game.dimensions.infrastructure.persistence.jdbc;

import com.udla.markenx.api.game.dimensions.domain.models.aggregates.Dimension;
import com.udla.markenx.api.game.dimensions.domain.ports.outgoing.DimensionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcDimensionRepository implements DimensionCommandRepository {

    @Override
    public Dimension save(Dimension task) {
        return null;
    }

    @Override
    public Dimension findById(String id) {
        return null;
    }
}
