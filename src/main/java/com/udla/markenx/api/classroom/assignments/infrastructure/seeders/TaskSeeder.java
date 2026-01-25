package com.udla.markenx.api.classroom.assignments.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
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
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Seeder idempotente para tareas.
 * Solo inserta tareas que no existen (verificadas por título y curso como clave natural).
 * Es seguro ejecutar múltiples veces sin duplicar datos.
 */
@Slf4j
@Component
@Profile("dev")
@Order(5)
@RequiredArgsConstructor
public class TaskSeeder extends BaseSeeder implements CommandLineRunner {

    private final QueryCourseUseCase queryCourseUseCase;
    private final CreateTaskUseCase createTaskUseCase;
    private final ScenarioQueryUseCase scenarioQueryUseCase;
    private final TaskQueryRepository taskQueryRepository;

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

    /**
     * Obtiene las claves (título-courseId) de las tareas existentes para un curso.
     */
    private Set<String> getExistingTaskKeys(String courseId) {
        return taskQueryRepository.findByCourseId(courseId).stream()
                .map(this::toTaskKey)
                .collect(Collectors.toSet());
    }

    private String toTaskKey(@NonNull Task task) {
        return task.getInfo().title() + "-" + task.getCourseId();
    }

    private void seedTasksForCourse(CoursePortDTO course, String scenarioId) {
        Set<String> existingKeys = getExistingTaskKeys(course.id());

        TaskSeedFactory.forActiveCourse(referenceTime()).stream()
                .filter(def -> !taskExists(def.title(), course.id(), existingKeys))
                .forEach(def -> createTask(def, course.id(), scenarioId));
    }

    /**
     * Verifica si una tarea ya existe usando título y courseId como clave natural.
     */
    private boolean taskExists(String title, String courseId, Set<String> existingKeys) {
        String key = title + "-" + courseId;
        boolean exists = existingKeys.contains(key);
        if (exists) {
            log.debug("Task already exists, skipping: title={}, courseId={}", title, courseId);
        }
        return exists;
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
        log.debug("Task created: title={}, courseId={}", def.title(), courseId);
    }

    @Contract(" -> new")
    private @NonNull LocalDateTime referenceTime() {
        return LocalDateTime.now();
    }
}
