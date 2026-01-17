package com.udla.markenx.api.classroom.terms.application.services;

import com.udla.markenx.api.classroom.terms.application.ports.out.TermCommandRepository;
import com.udla.markenx.api.classroom.terms.application.ports.out.TermQueryRepository;
import com.udla.markenx.api.classroom.terms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.classroom.terms.domain.models.valueobjects.TermStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RefreshTermStatusesService {

    private final TermQueryRepository queryRepository;
    private final TermCommandRepository commandRepository;

    public void handle() {
        List<AcademicTerm> terms = queryRepository.findByStatus(Set.of(TermStatus.ENDED.name()), true);
        for (AcademicTerm term: terms) {
            term.refreshStatus();
            commandRepository.save(term);
        }
    }
}
