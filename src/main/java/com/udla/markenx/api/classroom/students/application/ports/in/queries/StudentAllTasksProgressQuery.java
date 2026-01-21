package com.udla.markenx.api.classroom.students.application.ports.in.queries;

/**
 * Query to get all tasks with progress for a specific student.
 *
 * @param studentId The ID of the student
 */
public record StudentAllTasksProgressQuery(
        String studentId
) {
}
