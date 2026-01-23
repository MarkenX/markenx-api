package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Query repository for StudentTaskProgress read operations.
 */
public interface StudentTaskProgressQueryRepository {

    /**
     * Finds progress for a specific student and task combination.
     *
     * @param studentId The student ID
     * @param taskId The task ID
     * @return Optional containing the progress if found
     */
    Optional<StudentTaskProgress> findByStudentIdAndTaskId(
            @NonNull String studentId,
            @NonNull String taskId
    );

    /**
     * Finds all progress records for a specific student.
     *
     * @param studentId The student ID
     * @return List of progress records for the student
     */
    List<StudentTaskProgress> findByStudentId(@NonNull String studentId);

    /**
     * Finds all progress records for a specific task.
     *
     * @param taskId The task ID
     * @return List of progress records for the task
     */
    List<StudentTaskProgress> findByTaskId(@NonNull String taskId);

    /**
     * Finds all progress records with the specified statuses.
     *
     * @param statuses The set of status names to filter by
     * @return List of progress records matching the statuses
     */
    List<StudentTaskProgress> findByStatuses(@NonNull Set<String> statuses);
}
