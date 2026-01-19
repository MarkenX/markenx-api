package com.udla.markenx.api.classroom.assignments.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.CreateTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.FindAllCoursesIdsForAssignmentsHandler;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.CreateTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.AssignmentException;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
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
public class TaskSeeder implements CommandLineRunner {

    private final FindAllCoursesIdsForAssignmentsHandler findAllCoursesIdsForAssignments;
    private final CreateTaskUseCase createTaskUseCase;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding tasks...");

        List<String> coursesIds = findAllCoursesIdsForAssignments.handle();
        if (coursesIds.isEmpty()) {
            log.warn("No courses found, skipping task seeding.");
            return;
        }

        try {
            LocalDateTime deadline1 = LocalDateTime.now().plusDays(30);
            LocalDateTime deadline2 = LocalDateTime.now().plusDays(60);
            LocalDateTime deadline3 = LocalDateTime.now().plusDays(90);
            LocalDateTime historicalDeadline = LocalDateTime.now().minusDays(10);

            coursesIds.forEach(courseId -> {
                // Task 1: Simulacion de Lanzamiento
                Task task1 = createTaskUseCase.handle(new CreateTaskCommand(
                        "Simulacion de Lanzamiento",
                        "Realiza una simulacion de lanzamiento de producto y alcanza al menos 70% de aceptacion",
                        deadline1,
                        0.70,
                        courseId,
                        3,
                        false
                ));
                log.info("Created task: {} (id: {})", task1.getInfo().title(), task1.getId());

                // Task 2: Estrategia de Pricing
                Task task2 = createTaskUseCase.handle(new CreateTaskCommand(
                        "Estrategia de Pricing",
                        "Desarrolla una estrategia de precios efectiva para maximizar la aceptacion",
                        deadline2,
                        0.65,
                        courseId,
                        3,
                        false
                ));
                log.info("Created task: {} (id: {})", task2.getInfo().title(), task2.getId());

                // Task 3: Campana de Marketing Digital
                Task task3 = createTaskUseCase.handle(new CreateTaskCommand(
                        "Campana de Marketing Digital",
                        "Disena y ejecuta una campana de marketing digital exitosa",
                        deadline3,
                        0.75,
                        courseId,
                        2,
                        false
                ));
                log.info("Created task: {} (id: {})", task3.getInfo().title(), task3.getId());

                // Task 4: Tarea vencida (para probar estado OUTDATED)
                Task outdated = createTaskUseCase.handle(new CreateTaskCommand(
                        "Tarea Historica Vencida",
                        "Tarea historica para pruebas de estado OUTDATED",
                        historicalDeadline,
                        0.80,
                        courseId,
                        5,
                        true
                ));
                log.info("Created outdated task: {} (id: {})", outdated.getInfo().title(), outdated.getId());
            });

            log.info("Tasks seeded successfully.");
        } catch (AssignmentException e) {
            log.error(e.getMessage(), e);
            log.info("Tasks seeding failed.");
        }
    }
}
