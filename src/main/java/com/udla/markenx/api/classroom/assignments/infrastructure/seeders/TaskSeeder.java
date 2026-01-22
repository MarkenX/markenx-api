package com.udla.markenx.api.classroom.assignments.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.infrastructure.seeders.factories.TaskSeedFactory;
import com.udla.markenx.api.classroom.assignments.infrastructure.seeders.valueobjects.TaskSeedDefinition;
import com.udla.markenx.api.classroom.courses.application.ports.in.dtos.CoursePortDTO;
import com.udla.markenx.api.classroom.courses.application.ports.in.usecases.QueryCourseUseCase;
import com.udla.markenx.api.game.scenarios.application.ports.incoming.ScenarioQueryUseCase;
import com.udla.markenx.api.game.scenarios.application.queries.GetAllScenariosPaginatedQuery;
import com.udla.markenx.api.shared.infrastructure.seeders.BaseSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@Order(4)
@RequiredArgsConstructor
public class TaskSeeder extends BaseSeeder implements CommandLineRunner {

    private final QueryCourseUseCase queryCourseUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final ScenarioQueryUseCase scenarioQueryUseCase;

    @Override
    public String name() {
        return "Tasks";
    }

    @Override
    protected void doSeed() {
        String scenarioId = getFirstScenarioId();
        if (scenarioId == null) {
            log.warn("No scenarios found. Skipping task seeding.");
            return;
        }
        activeCourses().forEach(course -> seedTasksForCourse(course, scenarioId));
    }

    private @Nullable String getFirstScenarioId() {
        var scenarios = scenarioQueryUseCase.getAllPaginated(new GetAllScenariosPaginatedQuery(0, 1));
        return scenarios.isEmpty() ? null : scenarios.getContent().get(0).id();
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private List<CoursePortDTO> activeCourses() {
        return queryCourseUseCase.listCourses();
    }

    private void seedTasksForCourse(CoursePortDTO course, String scenarioId) {
        TaskSeedFactory.forActiveCourse(referenceTime())
                .forEach(def -> createTask(def, course.id(), scenarioId));
    }

    private void createTask(@NonNull TaskSeedDefinition def, String courseId, String scenarioId) {
        var command = new CreateTaskCommand(
                def.title(),
                def.description(),
                def.deadline(),
                def.acceptanceRate(),
                courseId,
                def.maxAttempts(),
                scenarioId,
                def.outdated()
        );

        createTaskUseCase.handle(command);
    }

    @Contract(" -> new")
    private @NonNull LocalDateTime referenceTime() {
        return LocalDateTime.now();
    }
}
