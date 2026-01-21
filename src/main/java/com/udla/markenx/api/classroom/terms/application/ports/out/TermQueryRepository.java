package com.udla.markenx.api.classroom.terms.application.ports.out;

import com.udla.markenx.api.classroom.terms.domain.models.aggregates.Term;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TermQueryRepository {
    Optional<Term> findById(@NonNull String id);
    Term findActiveTerm();
    Term findByIdOrThrow(@NonNull String id);
    List<Term> findAll();
    List<Term> findAllByYear(int year);
    List<Term> findAllByStatuses(@NonNull Set<String> statuses, boolean exclude);
    Page<Term> findAllPaginated(Pageable pageable);
}
