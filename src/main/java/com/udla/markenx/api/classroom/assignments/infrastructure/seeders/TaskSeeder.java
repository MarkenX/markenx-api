package com.udla.markenx.api.classroom.assignments.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.FindAllCoursesIdsForAssignmentsHandler;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.SaveTaskUseCase;
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
    private final SaveTaskUseCase saveTaskUseCase;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding tasks...");

        List<String> coursesIds = findAllCoursesIdsForAssignments.handle();

        try {
            LocalDateTime upcomingDeadline = LocalDateTime.now().plusDays(10);
            LocalDateTime historicalDeadline = LocalDateTime.now().minusDays(10);

            coursesIds.forEach(courseId -> {
                Task upcoming = saveTaskUseCase.handle(new SaveTaskCommand(
                        "Seed - Sin empezar",
                        "Tarea seeded en estado NOT_STARTED",
                        upcomingDeadline,
                        0.8,
                        courseId,
                        5,
                        false
                ));
                log.info("Created upcoming task: {}", upcoming);

                Task outdated = saveTaskUseCase.handle(new SaveTaskCommand(
                        "Seed - Vencida",
                        "Tarea seeded en estado OUTDATED",
                        historicalDeadline,
                        0.8,
                        courseId,
                        5,
                        true
                ));
                log.info("Created outdated task: {}", outdated);
            });

            log.info("Tasks seeded successfully.");
        } catch (AssignmentException e) {
            log.error(e.getMessage(), e);
            log.info("Tasks seeding failed.");
        }
    }
}
