package com.udla.markenx.api.classroom.assignments.infrastructure.listeners;

import com.udla.markenx.api.classroom.assignments.application.ports.in.commands.RegisterTaskAttemptResultCommand;
import com.udla.markenx.api.classroom.assignments.application.ports.in.usecases.UpdateTaskUseCase;
import com.udla.markenx.api.shared.domain.events.integration.AttemptResultRegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens to attempt result events from the game/attempts module
 * and updates the student's task progress and task status.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AttemptResultListener {

    private final UpdateTaskUseCase updateTaskUseCase;

    @EventListener
    public void on(AttemptResultRegisteredEvent event) {
        log.info("Received AttemptResultRegisteredEvent: attemptId={}, attemptId={}, studentId={}, score={}, approved={}",
                event.attemptId(), event.taskId(), event.studentId(), event.profileScore(), event.isApproved());

        var command = new RegisterTaskAttemptResultCommand(
                event.taskId(),
                event.studentId(),
                event.profileScore()
        );

        updateTaskUseCase.registerAttemptResult(command);

        log.info("Task {} and student {} progress updated after attempt result registration",
                event.taskId(), event.studentId());
    }
}
