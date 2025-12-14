package com.udla.markenx.api.academicterms.infrastructure.persistance.jpa;

import com.udla.markenx.api.shared.infrastructure.persistance.jpa.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "academic_terms")
public class AcademicTermJpaEntity extends JpaEntity {

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "year")
    private int year;

    @Column(name = "status")
    private String status;

    public AcademicTermJpaEntity(
            String id,
            LocalDate startDate,
            LocalDate endDate,
            int year,
            String status) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
        this.status = status;
    }
}

