package com.udla.markenx.api.attempts.domain.models.aggregates;

import com.udla.markenx.api.attempts.domain.exceptions.InvalidStudentIdException;
import com.udla.markenx.api.attempts.domain.exceptions.InvalidTaskIdException;
import com.udla.markenx.api.attempts.domain.models.valueobjects.AttemptResult;
import com.udla.markenx.api.attempts.domain.models.valueobjects.AttemptStatus;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class Attempt extends Entity {

    private final AttemptId id;
    private final AttemptResult result;
    private AttemptStatus status;
    private final String taskId;
    private final String studentId;

    private Attempt(
            AttemptId id,
            AttemptResult result,
            AttemptStatus status,
            String taskId,
            String studentId
    ) {
        super();
        this.id = id;
        this.result = result;
        this.status = status;
        this.taskId = validateTaskId(taskId);
        this.studentId = validateStudentId(studentId);
    }

    // region Validations

    public String validateTaskId(String taskId) {
        if (taskId == null || taskId.isBlank()) {
            throw new InvalidTaskIdException();
        }
        return taskId;
    }

    public String validateStudentId(String studentId) {
        if (studentId == null || studentId.isBlank()) {
            throw new InvalidStudentIdException();
        }
        return studentId;
    }

    // endregion
}
