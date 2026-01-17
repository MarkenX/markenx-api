package com.udla.markenx.api.classroom.terms.application.ports.out;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TermQueryRepository {
    List<AcademicTerm> findAll();

    Optional<AcademicTerm> findById(@NonNull String id);

    List<AcademicTerm> findAllByYear(int year);
    List<AcademicTerm> findAllByStatus(@NonNull Set<String> statuses, boolean exclude);
    Page<AcademicTerm> findAllPaginated(Pageable pageable);
    Optional<AcademicTerm> findActiveTerm();
}
