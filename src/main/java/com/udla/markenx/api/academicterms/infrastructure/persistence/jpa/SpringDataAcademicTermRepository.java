package com.udla.markenx.api.academicterms.infrastructure.persistence.jpa;

import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataAcademicTermRepository extends JpaRepository<@NotNull AcademicTermJpaEntity, @NotNull String> {
    List<AcademicTermJpaEntity> findByStatusNot(AcademicTermStatus status);
    List<AcademicTermJpaEntity> findByYear(int year);
    @NotNull Optional<AcademicTermJpaEntity> findById(@NotNull String id);
}