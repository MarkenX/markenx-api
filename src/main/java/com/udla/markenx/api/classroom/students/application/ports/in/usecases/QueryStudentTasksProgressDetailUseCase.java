package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentTaskProgressQuery;

import java.util.List;

/**
 * Use case for querying tasks with progress for a specific student.
 */
public interface QueryStudentTasksProgressDetailUseCase {

    /**
     * Gets all tasks for a student's course with the student's specific progress on each task.
     *
     * @param query The query containing studentId
     * @return List of tasks with student's progress
     */
    List<StudentTaskPortDTO> getAllTasksWithProgress(StudentAllTasksProgressQuery query);

    /**
     * Gets a specific task with the student's progress on it.
     *
     * @param query The query containing studentId and taskId
     * @return The task with student's progress
     */
    StudentTaskPortDTO getTaskWithProgress(StudentTaskProgressQuery query);
}
