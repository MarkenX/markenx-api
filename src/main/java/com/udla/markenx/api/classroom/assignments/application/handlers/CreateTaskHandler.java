package com.udla.markenx.api.classroom.assignments.application.handlers;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.dtos.TaskPortDTO;
import com.udla.markenx.api.classroom.assignments.application.ports.in.mappers.TaskPortMapper;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressCommandRepository;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.CourseHasNoStudentsException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.CourseNotInUpcomingTermException;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.ScenarioNotFoundException;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskCommandRepository;
import com.udla.markenx.api.classroom.courses.application.ports.in.queries.IsActiveCourseQuery;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.ValidateCourseUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentsByCourseQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsByCourseUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.ScenarioValidationPort;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateTaskHandler implements CreateTaskUseCase {

    private final ValidateCourseUseCase validateCourseUseCase;
    private final ScenarioValidationPort scenarioValidationPort;
    private final QueryStudentsByCourseUseCase queryStudentsByCourseUseCase;
    private final TaskCommandRepository repository;
    private final StudentTaskProgressCommandRepository progressRepository;
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
        if (!scenarioValidationPort.existsById(command.scenarioId())) {
            throw new ScenarioNotFoundException(command.scenarioId());
        }

        // Get students enrolled in the course
        var studentsQuery = new StudentsByCourseQuery(command.courseId());
        List<String> studentIds = queryStudentsByCourseUseCase.getStudentIdsByCourse(studentsQuery);

        if (studentIds.isEmpty()) {
            throw new CourseHasNoStudentsException(command.courseId());
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

        Task savedTask = repository.save(newTask);

        // Create student task progress records for each enrolled student
        for (String studentId : studentIds) {
            var progress = StudentTaskProgress.create(studentId, savedTask.getId());
            progressRepository.save(progress);
        }

        return mapper.toDTO(savedTask);
    }
}
