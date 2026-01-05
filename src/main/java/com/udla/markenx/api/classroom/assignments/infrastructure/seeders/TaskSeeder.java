package com.udla.markenx.api.classroom.assignments.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.commands.SaveTaskCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.FindAllCoursesIdsForAssignmentsHandler;
import com.udla.markenx.api.classroom.assignments.application.ports.incoming.SaveTaskUseCase;
import com.udla.markenx.api.classroom.assignments.domain.exceptions.AssignmentException;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
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
    private final Flyway flyway;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding tasks...");

        List<String> coursesIds = findAllCoursesIdsForAssignments.handle();

        try {
            String deadlineString = "2025-12-24T18:30:00";
            LocalDateTime deadline = LocalDateTime.parse(deadlineString);

            coursesIds.forEach(courseId -> {
                var query = new SaveTaskCommand(
                        "Test",
                        "Tarea de prueba",
                        deadline,
                        0.8,
                        courseId,
                        5,
                        true
                );
                Task saved = saveTaskUseCase.handle(query);
                log.info("The task {} was created", saved.toString());
            });
            log.info("Tasks seeded successfully.");
        } catch (AssignmentException e) {
            log.error(e.getMessage(), e);
            log.info("Tasks seeding failed.");
        }
    }
}
