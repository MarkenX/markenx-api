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

    /**
     * Represents the internal code of a course entity. This value is unique for each course
     * and is used as an internal identifier within the system.
     * <p>
     * Constraints:
     * - Cannot be null.
     * - Must be unique across all course records.
     * - Cannot be updated once assigned.
     * <p>
     * Mapping Details:
     * - Persisted in the database column "internal_code".
     * - Automatically generated using a sequence generator with the sequence name
     *   "course_internal_code_seq" and an allocation size of 1.
     */
    @Column(name = "internal_code", nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_internal_code_seq")
    @SequenceGenerator(
            name = "course_internal_code_seq",
            sequenceName = "course_internal_code_seq",
            allocationSize = 1
    )
    private Long internalCode;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_term_id", nullable = false)
    private AcademicTermJpaEntity academicTerm;

    public CourseJpaEntity(
        String id,
        LifecycleStatus lifecycleStatus,
        String name
    ) {
        super(id, lifecycleStatus);
        this.name = name;
    }
}
