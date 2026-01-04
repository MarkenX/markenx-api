package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.exceptions.*;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Assignment extends Entity {

    private final AssignmentId id;

    private long code;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private AssignmentStatus status;

    private String academicTermId;

    // region Constructors

    private Assignment(
            AssignmentId id,
            String title,
            String description,
            LocalDateTime deadline,
            AssignmentStatus status,
            String academicTermId) {
        super();
        this.id = id;
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.deadline = validateDeadline(deadline);
        this.status = status;
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    public Assignment(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            String title,
            String description,
            LocalDateTime deadline,
            AssignmentStatus status,
            String academicTermId) {
        super(lifecycleStatus);
        this.id = new AssignmentId(id);
        this.code = validateCode(code);
        this.title = validateTitle(title);
        this.description = validateDescription(description);
        this.deadline = validateDeadline(deadline);
        this.status = status;
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    // endregion

    // region Getters

    public String getId() {
        return this.id.value();
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public LocalDate getDeadlineDate() {
        return this.deadline.toLocalDate();
    }

    public LocalTime getDeadlineTime() {
        return this.deadline.toLocalTime();
    }

    public String getStatus() {
        return this.status.name();
    }

    public String getAcademicTermId() {
        return this.academicTermId;
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
            throw new InvalidAcademicTermIdException();
        }
        return academicTermId;
    }

    public String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidAssignmentTitleException();
        }
        return title;
    }

    public String validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidAssignmentDescriptionException();
        }
        return description;
    }

    public LocalDateTime validateDeadline(LocalDateTime dueDate) {
        if (dueDate == null) {
            throw new DueDateNotProvidedException();
        }
        if (!dueDate.isAfter(LocalDateTime.now())) {
            throw new DueDateMustBeInTheFutureException();
        }
        return dueDate;
    }

    // endregion

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
}
