package com.udla.markenx.api.classroom.students.application.ports.in.queries;

/**
 * Query to get a student's progress on a specific task.
 *
 * @param studentId The ID of the student
 * @param taskId The ID of the task
 */
public record StudentTaskProgressQuery(
        String studentId,
        String taskId
) {
}
