package com.udla.markenx.api.academicterms.domain.model.aggregates.academicTerm;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class AcademicTerm {

    private final AcademicTermId id;
    private final AcademicTermStatus status;
    private final int sequence;
    private final int year;

    @Getter(AccessLevel.NONE)
    private final DateInterval dateInterval;

    protected AcademicTerm(DateInterval dateInterval, int year, int sequence, AcademicTermStatus status) {
        this.id = AcademicTermId.generate();
        this.dateInterval = dateInterval;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }

    protected AcademicTerm(String id, DateInterval dateInterval, int year, int sequence, AcademicTermStatus status) {
        this.id = new AcademicTermId(id);
        this.dateInterval = dateInterval;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }

    public LocalDate getStartDate() {
        return this.dateInterval.startDate();
    }

    public LocalDate getEndDate() {
        return this.dateInterval.endDate();
    }

    public boolean overlapsWith(AcademicTerm term) {
        return dateInterval.overlapsWith(term.dateInterval);
    }

    private AcademicTermStatus updateStatus(LocalDate termStartDate, LocalDate termEndDate) {
        LocalDate today = LocalDate.now();
        if (today.isAfter(termStartDate) && today.isBefore(termEndDate)) {
            return AcademicTermStatus.ACTIVE;
        }
        if (today.isBefore(termStartDate)) {
            return AcademicTermStatus.UPCOMING;
        }
        return AcademicTermStatus.ENDED;
    }
}
