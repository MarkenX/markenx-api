package com.udla.markenx.api.classroom.assignments.application.handlers;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.mappers.TaskPortMapper;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.CourseNotInUpcomingTermException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.ScenarioNotFoundException;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskCommandRepository;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.IsActiveCourseQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.ValidateCourseUseCase;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.ValidateScenarioUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.ScenarioExistsQuery;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateTaskHandler implements CreateTaskUseCase {

    private final ValidateCourseUseCase validateCourseUseCase;
    private final ValidateScenarioUseCase validateScenarioUseCase;
    private final TaskCommandRepository repository;
    private final TaskPortMapper mapper = new TaskPortMapper();

    @Override
    public TaskPortDTO handle(@NonNull CreateTaskCommand command) {
        if (!command.isHistorical()) {
            var courseQuery = new IsActiveCourseQuery(command.courseId());
            if(!validateCourseUseCase.isActive(courseQuery)) {
                throw new CourseNotInUpcomingTermException(command.courseId());
            }
        }

        // Validate scenario exists
        var scenarioQuery = new ScenarioExistsQuery(command.scenarioId());
        if (!validateScenarioUseCase.exists(scenarioQuery)) {
            throw new ScenarioNotFoundException(command.scenarioId());
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
                    command.maxAttempts(),
                    command.scenarioId()
            );
        } else {
            newTask = Task.create(
                    info,
                    command.deadline(),
                    minScoreToPass,
                    command.courseId(),
                    command.maxAttempts(),
                    command.scenarioId()
            );
        }

        return mapper.toDTO(repository.save(newTask));
    }
}
