package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;

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
            long code,
            String title,
            String description,
            LocalDate dueDate,
            AssignmentStatus status,
            String academicTermId) {
        super();
        this.id = id;
        this.code = code;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status;
        this.academicTermId = academicTermId;
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
}
