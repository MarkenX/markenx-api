package com.udla.markenx.api.courses.infrastructure.persistence.jpa;

import com.udla.markenx.api.academicterms.infrastructure.persistence.jpa.AcademicTermJpaEntity;
import com.udla.markenx.api.shared.domain.models.valueobjects.LifecycleStatus;
import com.udla.markenx.api.shared.infrastructure.persistance.jpa.JpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "courses")
public class CourseJpaEntity extends JpaEntity {

    @Column(name = "internal_code", nullable = false, unique = true, updatable = false)
    private Long internalCode;

    @Column(name = "name")
    private String name;

    @Column(name = "academic_term_id")
    private String academicTermId;

    @PrePersist
    void prePersist() {
        if (internalCode == null) {
            internalCode = 1L;
        }
    }

    public CourseJpaEntity(
            String id,
            LifecycleStatus lifecycleStatus,
            String name,
            String academicTermId
    ) {
        super(id, lifecycleStatus);
        this.name = name;
        this.academicTermId = academicTermId;
    }
}
