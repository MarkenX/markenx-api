package com.udla.markenx.api.game.attempts.application.ports.in.mappers;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.infrastructure.web.rest.dtos.AttemptOutcome;

public class AttemptPortMapper {

    public AttemptPortDTO toDTO(Attempt domain) {
        return new AttemptPortDTO(
                domain.getId(),
                domain.getTaskId(),
                domain.getResult().profileScore(),
                domain.getResult().approvalRate(),
                domain.getResult().budgetRemaining(),
                domain.getTurnHistories().size(),
                domain.getStatus().name(),
                AttemptOutcome.from(domain.getStatus()).name(),
                domain.getSessionDate()
        );
    }
}
