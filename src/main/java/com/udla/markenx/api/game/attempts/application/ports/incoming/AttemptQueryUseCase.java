package com.udla.markenx.api.game.attempts.application.ports.incoming;

import com.udla.markenx.api.game.attempts.application.commands.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;

import java.util.List;

public interface AttemptQueryUseCase {
    GameSessionResponse getById(GetAttemptByIdQuery query);
    List<Attempt> getByTaskId(String taskId);
    List<Attempt> getByStudentId(String studentId);
}
