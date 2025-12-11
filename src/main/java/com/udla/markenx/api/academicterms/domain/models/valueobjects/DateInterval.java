package com.udla.markenx.api.academicterms.domain.models.valueobjects;

import com.udla.markenx.api.academicterms.domain.exceptions.EndDateCannotBeNullException;
import com.udla.markenx.api.academicterms.domain.exceptions.InvalidDateIntervalException;
import com.udla.markenx.api.academicterms.domain.exceptions.StartDateCannotBeNullException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record DateInterval(LocalDate startDate, LocalDate endDate) {

  public DateInterval(LocalDate startDate, LocalDate endDate) {
    this.startDate = validateStartDate(startDate);
    this.endDate = validateEndDate(endDate);
    validateInterval(this.startDate, this.endDate);
  }

  private LocalDate validateStartDate(LocalDate startDate) {
    if (startDate == null) {
      throw new StartDateCannotBeNullException();
    }
    return startDate;
  }

  private LocalDate validateEndDate(LocalDate endDate) {
    if (endDate == null) {
      throw new EndDateCannotBeNullException();
    }
    return endDate;
  }

  private void validateInterval(LocalDate startDate, LocalDate endDate) {
    if (endDate.isBefore(startDate)) {
      throw new InvalidDateIntervalException(startDate, endDate);
    }
  }

  public int getEndYear() {
    return endDate.getYear();
  }

  public boolean spansOneYear() {
    int startYear = startDate.getYear();
    int endYear = endDate.getYear();
    return startYear == endYear;
  }

  public boolean spansMultipleYears() {
    return !spansOneYear();
  }

  public long getMonthLength() {
    return ChronoUnit.MONTHS.between(startDate, endDate);
  }

  public boolean overlapsWith(DateInterval other) {
    return this.equals(other) || contains(other.startDate) || contains(other.endDate);
  }

  public boolean contains(LocalDate date) {
    return date.isAfter(startDate) && date.isBefore(endDate);
  }
}
