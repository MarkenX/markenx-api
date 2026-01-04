package com.udla.markenx.api.assignments.domain.models.aggregates;

import com.udla.markenx.api.assignments.domain.exceptions.*;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentDeadline;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentInfo;
import com.udla.markenx.api.assignments.domain.models.valueobjects.AssignmentStatus;
import com.udla.markenx.api.shared.domain.models.aggregates.Entity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@SuppressWarnings({"LombokGetterMayBeUsed"})
public class Assignment extends Entity {

    private final AssignmentId id;

    private long code;
    private AssignmentInfo info;
    private AssignmentDeadline deadline;
    private AssignmentStatus status;

    private String academicTermId;

    // region Constructors

    private Assignment(
            AssignmentId id,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentStatus status,
            String academicTermId) {
        super();
        this.id = id;
        this.info = info;
        this.deadline = deadline;
        this.status = status;
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    public Assignment(
            String id,
            LifecycleStatus lifecycleStatus,
            long code,
            AssignmentInfo info,
            AssignmentDeadline deadline,
            AssignmentStatus status,
            String academicTermId) {
        super(lifecycleStatus);
        this.id = new AssignmentId(id);
        this.code = validateCode(code);
        this.info = info;
        this.deadline = deadline;
        this.status = status;
        this.academicTermId = validateAcademicTermId(academicTermId);
    }

    // endregion

    // region Factories

    public static @NonNull Assignment create(AssignmentInfo info, LocalDateTime deadline, String academicTermId) {
        var id = AssignmentId.generate();
        return new Assignment(
                id,
                info,
                AssignmentDeadline.future(deadline),
                AssignmentStatus.NOT_STARTED,
                academicTermId
        );
    }

    public static @NonNull Assignment createHistorical(AssignmentInfo info, LocalDateTime deadline, String academicTermId) {
        var id = AssignmentId.generate();
        return new Assignment(
                id,
                info,
                AssignmentDeadline.historical(deadline),
                AssignmentStatus.NOT_STARTED,
                academicTermId
        );
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

    public String getAcademicTermId() {
        return this.academicTermId;
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
        this.academicTermId = validateAcademicTermId(academicTermId);
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

    // endregion

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
