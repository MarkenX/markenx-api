package com.udla.markenx.api.classroom.assignments.application.handlers;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.mappers.TaskPortMapper;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.EnsureCourseHasUpcomingTermForAssignment;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskCommandRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskHandler implements CreateTaskUseCase {

    private final EnsureCourseHasUpcomingTermForAssignment ensureCourseHasUpcomingTerm;
    private final TaskCommandRepository repository;

    private final TaskPortMapper mapper = new TaskPortMapper();

    @Override
    public TaskPortDTO handle(@NonNull CreateTaskCommand command) {
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

        return mapper.toDTO(repository.save(newTask));
    }
}
