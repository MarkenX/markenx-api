package com.udla.markenx.api.academicterms.infrastructure.persistance.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "academic_terms")
public class AcademicTermJpaEntity {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "year")
    private int year;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "status")
    private String status;

    public AcademicTermJpaEntity() {
    }

    public AcademicTermJpaEntity(String id,
                                 LocalDate startDate,
                                 LocalDate endDate,
                                 int year,
                                 int sequence,
                                 String status) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }
}

