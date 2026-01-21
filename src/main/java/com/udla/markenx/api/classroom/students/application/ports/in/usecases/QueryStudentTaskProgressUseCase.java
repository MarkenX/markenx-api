package com.udla.markenx.api.classroom.students.application.ports.in.usecases;

import com.udla.markenx.api.classroom.students.application.ports.in.dtos.StudentTaskProgressPortDTO;
import com.udla.markenx.api.classroom.students.application.ports.in.queries.StudentTaskProgressQuery;

/**
 * Use case for querying a student's progress on a specific task.
 */
public interface QueryStudentTaskProgressUseCase {

    /**
     * Gets the student's progress on a specific task.
     *
     * @param query The query containing studentId and taskId
     * @return The student's task progress
     */
    StudentTaskProgressPortDTO getProgress(StudentTaskProgressQuery query);
}
