package com.udla.markenx.api.classroom.terms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermIdQueryCriteria;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermPageQueryCriteria;
import com.udla.markenx.api.classroom.terms.application.ports.in.queries.TermStatusQueryCriteria;
import com.udla.markenx.api.classroom.terms.application.ports.in.dtos.TermPortDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QueryTermsUseCase {
    TermPortDTO getActiveTerm();
    TermPortDTO getTermById(TermIdQueryCriteria criteria);
    List<TermPortDTO> listTerms();
    Page<TermPortDTO> listTermsPage(TermPageQueryCriteria criteria);
    List<TermPortDTO> listTermsByStatus(TermStatusQueryCriteria criteria);
}
