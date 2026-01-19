package com.udla.markenx.api.game.attempts.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.incoming.TaskQueryUseCase;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.StudentQueryUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.GetAllStudentsPaginatedQuery;
import com.udla.markenx.api.classroom.students.query.models.StudentSummaryReadModel;
import com.udla.markenx.api.game.attempts.application.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.commands.RegisterGameSessionCommand.TurnHistoryDTO;
import com.udla.markenx.api.game.attempts.application.dtos.GameSessionResponse;
import com.udla.markenx.api.game.attempts.application.ports.incoming.RegisterGameSessionUseCase;
import com.udla.markenx.api.game.attempts.domain.exceptions.AttemptException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@Profile("dev")
@Order(6)
@RequiredArgsConstructor
public class AttemptSeeder implements CommandLineRunner {

    private final RegisterGameSessionUseCase registerGameSessionUseCase;
    private final TaskQueryUseCase taskQueryUseCase;
    private final StudentQueryUseCase studentQueryUseCase;

    @Override
    public void run(String @NonNull ... args) {
        log.info("Seeding attempts...");

        List<Task> tasks = taskQueryUseCase.getAll();
        Page<StudentSummaryReadModel> studentsPage = studentQueryUseCase.getAllPaginated(
                new GetAllStudentsPaginatedQuery(0, 100)
        );
        List<StudentSummaryReadModel> students = studentsPage.getContent();

        if (tasks.isEmpty() || students.isEmpty()) {
            log.warn("No tasks or students found, skipping attempt seeding.");
            return;
        }

        try {
            int attemptCount = 0;

            // Create attempts for first 2 tasks and first 2 students
            for (int i = 0; i < Math.min(2, tasks.size()); i++) {
                Task task = tasks.get(i);
                if (task.isOutdated()) continue;

                for (int j = 0; j < Math.min(2, students.size()); j++) {
                    StudentSummaryReadModel student = students.get(j);

                    // Create an attempt with varying results
                    boolean isApproved = (i + j) % 2 == 0;
                    double finalAcceptance = isApproved ? 0.75 + (j * 0.05) : 0.55 + (j * 0.05);
                    double profileScore = isApproved ? 0.80 + (j * 0.05) : 0.50 + (j * 0.05);
                    int turnsUsed = 4 + j;
                    BigDecimal remainingBudget = new BigDecimal("200.00").subtract(
                            new BigDecimal(j * 30)
                    );

                    List<TurnHistoryDTO> history = buildTurnHistory(turnsUsed, finalAcceptance, remainingBudget);

                    var command = new RegisterGameSessionCommand(
                            task.getId(),
                            student.studentId(),
                            LocalDateTime.now().minusDays(5 - j),
                            finalAcceptance,
                            remainingBudget,
                            turnsUsed,
                            profileScore,
                            history
                    );

                    GameSessionResponse response = registerGameSessionUseCase.handle(command);
                    log.info("Attempt created: student={}, task={}, outcome={} (id: {})",
                            student.fullName(),
                            task.getInfo().title(),
                            response.finalOutcome(),
                            response.id()
                    );
                    attemptCount++;
                }
            }

            log.info("Attempts seeded successfully. Total: {}", attemptCount);
        } catch (AttemptException e) {
            log.error(e.getMessage(), e);
            log.info("Attempts seeding failed.");
        }
    }

    private List<TurnHistoryDTO> buildTurnHistory(int totalTurns, double finalAcceptance, BigDecimal finalBudget) {
        List<TurnHistoryDTO> history = new ArrayList<>();
        double startAcceptance = 0.30;
        BigDecimal startBudget = new BigDecimal("500.00");

        for (int turn = 1; turn <= totalTurns; turn++) {
            double progress = (double) turn / totalTurns;
            double acceptance = startAcceptance + (finalAcceptance - startAcceptance) * progress;
            BigDecimal budget = startBudget.subtract(
                    startBudget.subtract(finalBudget).multiply(BigDecimal.valueOf(progress))
            );

            String event = turn == 3 ? "Tendencia Viral" : null;

            history.add(new TurnHistoryDTO(
                    turn,
                    Math.round(acceptance * 10000.0) / 10000.0,
                    budget.setScale(2, java.math.RoundingMode.HALF_UP),
                    event,
                    Collections.emptyList()
            ));
        }

        return history;
    }
}
