package com.udla.markenx.api.classroom.assignments.application.ports.out;

import com.udla.markenx.api.classroom.assignments.domain.models.entities.StudentTaskProgress;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

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
}
