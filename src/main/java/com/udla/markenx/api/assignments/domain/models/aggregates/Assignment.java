package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.exceptions.InvalidAcademicTermIdException;
import com.udla.markenx.api.assignments.domain.exceptions.InvalidAssignmentCodeException;
import com.udla.markenx.api.assignments.domain.exceptions.InvalidAssignmentTitleException;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;

import java.time.LocalDate;

@SuppressWarnings("LombokGetterMayBeUsed")
public class Assignment extends Entity {

    private final AssignmentId id;

    private long code;
    private String title;
    private String description;
    private LocalDate dueDate;
    private AssignmentStatus status;

    private String academicTermId;

    // region Constructors

    private Assignment(
            AssignmentId id,
            String title,
            String description,
            LocalDate dueDate,
            AssignmentStatus status,
            String academicTermId) {
        super();
        this.id = id;
        this.title = validateTitle(title);
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    public Assignment(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            String title,
            String description,
            LocalDate dueDate,
            AssignmentStatus status,
            String academicTermId) {
        super(lifecycleStatus);
        this.id = new AssignmentId(id);
        this.code = validateCode(code);
        this.title = validateTitle(title);
        this.description = description;
        this.dueDate = dueDate;
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

    public LocalDate getDueDate() {
        return this.dueDate;
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

    // endregion
}
