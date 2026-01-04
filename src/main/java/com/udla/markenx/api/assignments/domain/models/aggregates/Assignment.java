package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.exceptions.*;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentDeadline;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentScore;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@SuppressWarnings({"LombokGetterMayBeUsed"})
public abstract class Assignment extends Entity {

    private final AssignmentId id;

    private long code;
    private AssignmentInfo info;
    private AssignmentDeadline deadline;
    private AssignmentScore minScoreToPass;
    private AssignmentStatus status;

    private String courseId;

    // region Constructors

    protected Assignment(
            AssignmentId id,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentStatus status,
            String courseId) {
        super();
        this.id = id;
        this.info = info;
        this.deadline = deadline;
        this.status = status;
        this.courseId = validateAcademicTermId(courseId);
    }

    public Assignment(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentStatus status,
            String courseId) {
        super(lifecycleStatus);
        this.id = new AssignmentId(id);
        this.code = validateCode(code);
        this.info = info;
        this.deadline = deadline;
        this.status = status;
        this.courseId = validateAcademicTermId(courseId);
    }

    // endregion

    // region Getters

    public String getId() {
        return this.id.value();
    }

    public AssignmentInfo getInfo() {
        return this.info;
    }

    public AssignmentDeadline getDeadline() {
        return this.deadline;
    }

    public LocalDate getDeadlineDate() {
        return this.deadline.date();
    }

    public LocalTime getDeadlineTime() {
        return this.deadline.time();
    }

    public AssignmentStatus getStatus() {
        return this.status;
    }

    public String getCourseId() {
        return this.courseId;
    }

    // endregion

    // region Setters

    public void updateInfo(AssignmentInfo newInfo) {
        this.info = newInfo;
    }

    public void reschedule(AssignmentDeadline newDeadline) {
        this.deadline = newDeadline;
    }

    public void changeAcademicTerm(String academicTermId) {
        this.courseId = validateAcademicTermId(academicTermId);
    }

    // endregion

    // region Validations

    public long validateCode(long code) {
        if (code <= 0) {
            throw new InvalidAssignmentCodeException();
        }
        return code;
    }

    public String validateAcademicTermId(String academicTermId) {
        if (academicTermId == null || academicTermId.isBlank()) {
            throw new InvalidCourseIdException();
        }
        return academicTermId;
    }

    // endregion

    public abstract void updateStatus();

    protected void transitionTo(AssignmentStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new InvalidAssignmentStatusTransitionException(status, next);
        }
        this.status = next;
    }

    // region Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment that)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    // endregion

    @Override
    public String toString() {
        return String.format("ASN-%d", code);

    }
}
