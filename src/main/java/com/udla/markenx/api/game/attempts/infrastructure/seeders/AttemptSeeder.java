package com.udla.markenx.api.game.attempts.infrastructure.seeders;

import com.udla.markenx.api.classroom.assignments.application.ports.in.queries.IsTaskOutdatedQuery;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.ValidateTaskUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskProgressDetailPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentTasksProgressDetailUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.usecases.QueryStudentsDetailUseCase;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentPageQueryCriteria;
import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentDetailPortDTO;
import com.udla.markenx.api.game.attempts.application.ports.in.commands.RegisterGameSessionCommand;
import com.udla.markenx.api.game.attempts.application.ports.in.usecases.RegisterGameSessionUseCase;
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

    private void seedAttemptsForStudent(@NonNull StudentDetailPortDTO student) {
        var query = new StudentAllTasksProgressQuery(student.studentId());
        var tasks = queryStudentTasksProgressDetailUseCase.getAllTasksWithProgress(query);

        tasks.stream()
                .filter(this::isValidTask)
                .forEach(task ->
                        seedAttempts(student, task)
                );
    }

    private void seedAttempts(@NonNull StudentDetailPortDTO student, @NonNull StudentTaskProgressDetailPortDTO task) {
        var now = LocalDateTime.now();

        var success = AttemptSeedFactory.successful(now, 0);
        var failure = AttemptSeedFactory.failed(now, 1);

        registerAttempt(task.taskId(), student.studentId(), success);
        registerAttempt(task.taskId(), student.studentId(), failure);
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

    private boolean isValidTask(@NonNull StudentTaskProgressDetailPortDTO task) {
        return !validateTaskUseCase.isOutdated(
                new IsTaskOutdatedQuery(task.taskId())
        );
    }
}

