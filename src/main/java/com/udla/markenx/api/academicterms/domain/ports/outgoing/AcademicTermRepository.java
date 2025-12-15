package com.udla.markenx.api.academicterms.domain.ports.outgoing;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AcademicTermRepository {
    AcademicTerm save(AcademicTerm newAcademicTerm);
    Optional<AcademicTerm> findById(String id);
    List<AcademicTerm> findAll();
    List<AcademicTerm> findAllByYear(int year);
    List<AcademicTerm> findByStatusNot(AcademicTermStatus status);
    Page<@NotNull AcademicTerm> findAllPaginated(Pageable pageable);
}
