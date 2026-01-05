package com.udla.markenx.api.attempts.domain.models.aggregates;

import com.udla.markenx.api.attempts.domain.exceptions.InvalidTaskIdException;
import com.udla.markenx.api.attempts.domain.models.valueobjects.AttemptResult;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

public class Attempt extends Entity {

    private final AttemptId id;
    private final AttemptResult result;
    private final String taskId;

    private Attempt(
            AttemptId id,
            AttemptResult result,
            String taskId
    ) {
        super();
        this.id = id;
        this.result = result;
        this.taskId = validateTaskId(taskId);
    }

    // region Validations

    public String validateTaskId(String taskId) {
        if (taskId == null || taskId.isBlank()) {
            throw new InvalidTaskIdException();
        }
        return taskId;
    }

    // endregion
}
