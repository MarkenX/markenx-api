package com.udla.markenx.api.classroom.academicterms.application.ports.out;

import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface TermQueryRepository {
    List<AcademicTerm> findAll();
    List<AcademicTerm> findAllByYear(int year);
    List<AcademicTerm> findByStatus(@NonNull Set<String> statuses, boolean exclude);
    Page<AcademicTerm> findAllPaginated(Pageable pageable);
    AcademicTerm findActiveTerm();
}
