package com.udla.markenx.api.academicterms.domain.ports.outgoing;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AcademicTermQueryRepository {
    List<AcademicTerm> findAll();
    List<AcademicTerm> findAllByYear(int year);
    List<AcademicTerm> findByStatusNot(AcademicTermStatus status);
    Page<AcademicTerm> findAllPaginated(Pageable pageable);
}
