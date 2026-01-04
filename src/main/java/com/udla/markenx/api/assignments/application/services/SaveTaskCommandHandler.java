package com.udla.markenx.api.assignments.application.services;

import com.udla.markenx.api.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.assignments.domain.ports.outgoing.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveTaskCommandHandler implements SaveTaskUseCase {

    private final TaskCommandRepository repository;

    @Override
    public Task handle(@NonNull SaveTaskCommand command) {

        var info = new AssignmentInfo(command.title(), command.summary());
        var minScoreToPass = new AssignmentScore(command.minScoreToPass());

        Task newTask;
        if (command.isHistorical()) {
            newTask = Task.createHistorical(
                    info,
                    command.deadline(),
                    minScoreToPass,
                    command.courseId(),
                    command.maxAttempts()
            );
        } else {
            newTask = Task.create(
                    info,
                    command.deadline(),
                    minScoreToPass,
                    command.courseId(),
                    command.maxAttempts()
            );
        }

        return repository.save(newTask);
    }
}
