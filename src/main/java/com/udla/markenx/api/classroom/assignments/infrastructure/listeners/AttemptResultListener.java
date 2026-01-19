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
 * and updates the corresponding task's attempt counter and status.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AttemptResultListener {

    private final UpdateTaskUseCase updateTaskUseCase;

    @EventListener
    public void on(AttemptResultRegisteredEvent event) {
        log.info("Received AttemptResultRegisteredEvent: attemptId={}, taskId={}, score={}, approved={}",
                event.attemptId(), event.taskId(), event.profileScore(), event.isApproved());

        var command = new RegisterTaskAttemptResultCommand(
                event.taskId(),
                event.profileScore()
        );

        updateTaskUseCase.registerAttemptResult(command);

        log.info("Task {} updated after attempt result registration", event.taskId());
    }
}
