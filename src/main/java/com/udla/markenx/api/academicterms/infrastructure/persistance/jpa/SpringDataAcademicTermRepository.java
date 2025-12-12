package com.udla.markenx.api.academicterms.infrastructure.persistance.jpa;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAcademicTermRepository extends JpaRepository<@NotNull AcademicTermJpaEntity, @NotNull String> { }