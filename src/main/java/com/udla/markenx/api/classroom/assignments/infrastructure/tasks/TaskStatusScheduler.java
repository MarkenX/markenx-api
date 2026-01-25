package com.udla.markenx.api.classroom.assignments.infrastructure.tasks;

import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressCommandRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.StudentTaskProgressQueryRepository;
import com.udla.markenx.api.classroom.assignments.application.ports.out.TaskQueryRepository;
import com.udla.markenx.api.classroom.assignments.domain.models.aggregates.Task;
import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import com.udla.markenx.api.classroom.assignments.domain.models.valueobjects.AssignmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskStatusScheduler {

    private static final Set<String> PROCESSABLE_STATUSES = Set.of(
            AssignmentStatus.IN_PROGRESS.name()
    );

    private final StudentTaskProgressQueryRepository progressQueryRepository;
    private final StudentTaskProgressCommandRepository progressCommandRepository;
    private final TaskQueryRepository taskQueryRepository;

    @Scheduled(cron = "0 * * * * *")
    public void checkAndUpdateProgressStatuses() {
        // 1. Get all progress records with processable statuses
        List<StudentTaskProgress> progressRecords = progressQueryRepository.findByStatuses(PROCESSABLE_STATUSES);

        if (progressRecords.isEmpty()) {
            return;
        }

        // 2. Get unique task IDs and fetch tasks
        Set<String> taskIds = progressRecords.stream()
                .map(StudentTaskProgress::getTaskId)
                .collect(Collectors.toSet());

        Map<String, Task> tasksById = taskIds.stream()
                .map(taskQueryRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .collect(Collectors.toMap(Task::getId, Function.identity()));

        // 3. Check each progress and mark as failed if task is overdue
        int updatedCount = 0;
        for (StudentTaskProgress progress : progressRecords) {
            Task task = tasksById.get(progress.getTaskId());
            if (task == null) {
                continue;
            }

            AssignmentStatus previousStatus = progress.getStatus();
            progress.markAsFailedIfOverdue(task.getDeadline().value());

            if (progress.getStatus() != previousStatus) {
                progressCommandRepository.save(progress);
                updatedCount++;
                log.debug("Updated progress status for student {} on task {} from {} to {}",
                        progress.getStudentId(), progress.getTaskId(), previousStatus, progress.getStatus());
            }
        }

        if (updatedCount > 0) {
            log.info("Updated {} progress records to FAILED status due to overdue deadlines", updatedCount);
        }
    }
}
