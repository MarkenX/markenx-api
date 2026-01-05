package com.udla.markenx.api.game.attempts.domain.models.aggregates;

import com.udla.markenx.api.attempts.domain.exceptions.*;
import com.udla.markenx.api.game.attempts.domain.exceptions.*;
import com.udla.markenx.api.videogame.attempts.domain.exceptions.*;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptResult;
import com.udla.markenx.api.game.attempts.domain.models.valueobjects.AttemptStatus;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Attempt {

    private final AttemptId id;
    private AttemptResult result;
    private AttemptStatus status;
    private final String taskId;
    private final String studentId;

    // region Constructors

    private Attempt(
            AttemptId id,
            AttemptResult result,
            AttemptStatus status,
            String taskId,
            String studentId
    ) {
        this.id = id;
        this.result = result;
        this.status = status;
        this.taskId = validateTaskId(taskId);
        this.studentId = validateStudentId(studentId);
    }

    public Attempt(
            String id,
            int currentTurn,
            BigDecimal budgetRemaining,
            double approvalRate,
            double profileScore,
            AttemptStatus status,
            String taskId,
            String studentId
    ) {
        this.id = new AttemptId(id);
        this.result = new AttemptResult(currentTurn, budgetRemaining, approvalRate, profileScore);
        this.status = status;
        this.taskId = validateTaskId(taskId);
        this.studentId = validateStudentId(studentId);
    }

    // endregion

    // region Factories

    public static @NonNull Attempt create(
            String taskId,
            String studentId
    ) {
        var id = AttemptId.generate();
        return new Attempt(
                id,
                null,
                AttemptStatus.UNKNOWN,
                taskId,
                studentId
        );
    }

    // endregion

    // region Getters

    public String getId() {
        return this.id.value();
    }

    public AttemptResult getResult() {
        return this.result;
    }

    public AttemptStatus getStatus() {
        return this.status;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    // endregion

    public void registerResults(AttemptResult result, double minScoreToPass) {
        if (this.result != null) {
            throw new ResultsAlreadyRegisteredException();
        }
        this.result = validateResult(result);
        determineAndSetStatus(minScoreToPass);
    }

    private void determineAndSetStatus(double minScoreToPass) {
        if (this.result.approvalRate() >= minScoreToPass) {
            transitionTo(AttemptStatus.APPROVED);
        } else {
            transitionTo(AttemptStatus.DISAPPROVED);
        }
    }

    protected void transitionTo(AttemptStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new InvalidAttemptStatusTransitionException(status, next);
        }
        this.status = next;
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

    public AttemptResult validateResult(AttemptResult result) {
        if (this.result == null) {
            throw new NullAttemptResultException();
        }
        return this.result;
    }

    // endregion

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attempt that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion
}
