package com.udla.markenx.api.game.attempts.application.ports.in.usecases;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import com.udla.markenx.api.game.attempts.application.ports.in.commands.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;

import java.util.List;

public interface AttemptQueryUseCase {
    GameSessionResponse getById(GetAttemptByIdQuery query);
    List<AttemptPortDTO> getByTaskId(String taskId);
    List<AttemptPortDTO> getByStudentId(String studentId);
}
