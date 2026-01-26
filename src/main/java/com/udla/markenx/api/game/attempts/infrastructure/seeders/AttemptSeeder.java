package com.udla.markenx.api.game.attempts.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.IsTaskOutdatedQuery;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.ValidateTaskUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentTasksProgressDetailUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsDetailUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.RegisterGameSessionUseCase;
import com.udla.markenx.api.game.attempts.application.ports.out.AttemptQueryRepository;
import com.udla.markenx.api.game.attempts.domain.models.aggregates.Attempt;
import com.udla.markenx.api.game.attempts.infrastructure.seeders.factories.AttemptSeedFactory;
import com.udla.markenx.api.game.attempts.infrastructure.seeders.valueobjects.AttemptSeedDefinition;
import com.udla.markenx.api.shared.infrastructure.seeders.BaseSeeder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Seeder idempotente para intentos de juego.
 * Solo inserta intentos que no existen (verificados por studentId y taskId como clave natural).
 * Es seguro ejecutar múltiples veces sin duplicar datos.
 */
@Slf4j
@Component
@Profile("dev")
@Order(6)
@RequiredArgsConstructor
public class AttemptSeeder extends BaseSeeder implements CommandLineRunner {

    private final RegisterGameSessionUseCase registerGameSessionUseCase;
    private final QueryStudentsDetailUseCase queryStudentsDetailUseCase;
    private final QueryStudentTasksProgressDetailUseCase queryStudentTasksProgressDetailUseCase;
    private final ValidateTaskUseCase validateTaskUseCase;
    private final AttemptQueryRepository attemptQueryRepository;

    @Override
    public String name() {
        return "Attempts";
    }

    @Override
    protected void doSeed() {
        students().forEach(this::seedAttemptsForStudent);
    }

    @Override
    public void run(String @NonNull ... args) {
        seed();
    }

    private @NonNull List<StudentDetailPortDTO> students() {
        return queryStudentsDetailUseCase
                .listStudentsPage(new StudentPageQueryCriteria(0, 50))
                .getContent();
    }

    /**
     * Obtiene las claves (studentId-taskId) de los intentos existentes para un estudiante.
     */
    private Set<String> getExistingAttemptKeys(String studentId) {
        return attemptQueryRepository.findByStudentId(studentId).stream()
                .map(this::toAttemptKey)
                .collect(Collectors.toSet());
    }

    private String toAttemptKey(@NonNull Attempt attempt) {
        return attempt.getStudentId() + "-" + attempt.getTaskId();
    }

    private void seedAttemptsForStudent(@NonNull StudentDetailPortDTO student) {
        var query = new StudentAllTasksProgressQuery(student.id());
        var tasks = queryStudentTasksProgressDetailUseCase.getAllTasksWithProgress(query);
        Set<String> existingKeys = getExistingAttemptKeys(student.id());

        tasks.stream()
                .filter(this::isValidTask)
                .filter(task -> !attemptsExist(student.id(), task.taskId(), existingKeys))
                .forEach(task -> seedAttempts(student, task));
    }

    /**
     * Verifica si ya existen intentos para este estudiante y tarea.
     */
    private boolean attemptsExist(String studentId, String taskId, Set<String> existingKeys) {
        String key = studentId + "-" + taskId;
        boolean exists = existingKeys.contains(key);
        if (exists) {
            log.debug("Attempts already exist, skipping: studentId={}, taskId={}", studentId, taskId);
        }
        return exists;
    }

    private void seedAttempts(@NonNull StudentDetailPortDTO student, @NonNull StudentTaskPortDTO task) {
        var now = LocalDateTime.now();

        var success = AttemptSeedFactory.successful(now, 0);
        var failure = AttemptSeedFactory.failed(now, 1);

        registerAttempt(task.taskId(), student.id(), success);
        registerAttempt(task.taskId(), student.id(), failure);

        log.debug("Attempts seeded: studentId={}, taskId={}", student.id(), task.taskId());
    }

    private void registerAttempt(
            String taskId,
            String studentId,
            @NonNull AttemptSeedDefinition attempt
    ) {
        var command = new RegisterGameSessionCommand(
                taskId,
                studentId,
                attempt.startedAt(),
                attempt.finalAcceptance(),
                attempt.remainingBudget(),
                attempt.turnsUsed(),
                attempt.profileScore(),
                attempt.history()
        );

        registerGameSessionUseCase.handle(command);
    }

    private boolean isValidTask(@NonNull StudentTaskPortDTO task) {
        return !validateTaskUseCase.isOutdated(
                new IsTaskOutdatedQuery(task.taskId())
        );
    }
}
