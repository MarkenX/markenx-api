package com.udla.markenx.api.classroom.assignments.application.services;

import com.udla.markenx.api.classroom.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.EnsureCourseHasUpcomingTermForAssignment;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.classroom.assignments.domain.ports.outgoing.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveTaskCommandHandler implements SaveTaskUseCase {

    private final EnsureCourseHasUpcomingTermForAssignment ensureCourseHasUpcomingTerm;
    private final TaskCommandRepository repository;

    @Override
    public Task handle(@NonNull SaveTaskCommand command) {
        if (!command.isHistorical()) {
            ensureCourseHasUpcomingTerm.ensureCourseHasUpcomingTerm(command.courseId());
        }

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
