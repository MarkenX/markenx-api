package com.udla.markenx.api.game.attempts.application.ports.mappers;

import com.udla.markenx.api.game.attempts.application.ports.dtos.AttemptPortDTO;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;

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
                domain.getSessionDate()
        );
    }
}
