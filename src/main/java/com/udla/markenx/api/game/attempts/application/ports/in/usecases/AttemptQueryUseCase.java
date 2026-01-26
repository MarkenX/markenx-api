package com.udla.markenx.api.game.attempts.application.ports.in.usecases;

import com.udla.markenx.api.game.attempts.application.ports.in.dtos.AttemptPortDTO;
import com.udla.markenx.api.game.attempts.application.ports.in.queries.GetAttemptByIdQuery;
import com.udla.markenx.api.game.attempts.application.ports.in.dtos.GameSessionResponse;

import java.util.List;

public interface AttemptQueryUseCase {
    GameSessionResponse getById(GetAttemptByIdQuery query);
    List<AttemptPortDTO> listAttemptsByTaskId(String taskId);
    List<AttemptPortDTO> listAttemptsByStudentId(String studentId);
    List<AttemptPortDTO> listAttemptsByTaskIdAndStudentId(String taskId, String studentId);
}
