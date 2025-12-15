package com.udla.markenx.api.academicterms.domain.ports.outgoing;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AcademicTermRepository {
    AcademicTerm save(AcademicTerm newAcademicTerm);
    List<AcademicTerm> findAll();
    Page<@NotNull AcademicTerm> findAllPaginated(Pageable pageable);
}
