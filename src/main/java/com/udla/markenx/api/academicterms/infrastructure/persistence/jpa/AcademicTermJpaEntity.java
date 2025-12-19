package com.udla.markenx.api.academicterms.infrastructure.persistence.jpa;

import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.courses.infrastructure.persistence.jpa.CourseJpaEntity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.shared.infrastructure.persistance.jpa.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "academic-terms")
public class AcademicTermJpaEntity extends JpaEntity {

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "year")
    private int year;

    @Column(name = "sequence")
    private int sequence;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AcademicTermStatus status;

    @OneToMany(
            mappedBy = "academicTerm",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CourseJpaEntity> courses = new ArrayList<>();

    public AcademicTermJpaEntity(
            String id,
            LifecycleStatus lifecycleStatus,
            LocalDate startDate,
            LocalDate endDate,
            int year,
            int sequence,
            AcademicTermStatus status) {
        super(id, lifecycleStatus);
        this.startDate = startDate;
        this.endDate = endDate;
        this.year = year;
        this.sequence = sequence;
        this.status = status;
    }
}

