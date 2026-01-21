package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskProgressDetailPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentAllTasksProgressQuery;

import java.util.List;

/**
 * Use case for querying all tasks with progress for a specific student.
 */
public interface QueryStudentAllTasksProgressUseCase {

    /**
     * Gets all tasks for a student's course with the student's specific progress on each task.
     *
     * @param query The query containing studentId
     * @return List of tasks with student's progress
     */
    List<StudentTaskProgressDetailPortDTO> getAllTasksWithProgress(StudentAllTasksProgressQuery query);
}
