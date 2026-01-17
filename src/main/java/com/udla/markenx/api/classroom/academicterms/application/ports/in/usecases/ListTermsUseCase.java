package com.udla.markenx.api.classroom.academicterms.application.ports.in.usecases;

import com.udla.markenx.api.classroom.academicterms.application.ports.in.queries.TermPageQueryCriteria;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.queries.TermStatusQueryCriteria;
import com.udla.markenx.api.classroom.academicterms.application.ports.in.dtos.TermPortDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ListTermsUseCase {
    List<TermPortDTO> listTerms();
    TermPortDTO getActiveTerm();
    List<TermPortDTO> listTermsByStatus(TermStatusQueryCriteria criteria);
    Page<TermPortDTO> listTermsPage(TermPageQueryCriteria criteria);
}
