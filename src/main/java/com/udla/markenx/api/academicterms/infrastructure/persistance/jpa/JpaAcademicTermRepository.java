package com.udla.markenx.api.academicterms.infrastructure.persistance.jpa;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermRepository;
import com.udla.markenx.api.academicterms.domain.services.AcademicTermDomainService;
import com.udla.markenx.api.academicterms.infrastructure.mappers.AcademicTermJpaMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class JpaAcademicTermRepository implements AcademicTermRepository {

    private final SpringDataAcademicTermRepository springRepo;
    private final AcademicTermJpaMapper mapper;

    public JpaAcademicTermRepository(SpringDataAcademicTermRepository springRepo, AcademicTermJpaMapper mapper) {
        this.springRepo = springRepo;
        this.mapper = mapper;
    }

    /**
     * Saves the given academic term to the database.
     *
     * @param newAcademicTerm the academic term to be saved
     * @return the saved academic term mapped to its domain representation
     */
    @Override
    public AcademicTerm save(AcademicTerm newAcademicTerm) {
        AcademicTermJpaEntity saved = springRepo.save(mapper.toEntity(newAcademicTerm));
        return mapper.toDomain(saved, newAcademicTerm.getSequence());
    }

    /**
     * Retrieves a paginated list of all academic terms, ordered by their start date.
     *
     * @param pageable the pagination and sorting information
     * @return a page of academic terms, each mapped to the domain model and ordered by start date
     */
    @Override
    public Page<@NotNull AcademicTerm> findAllPaginated(Pageable pageable) {
        Page<@NotNull AcademicTermJpaEntity> page = springRepo.findAll(pageable);
        List<AcademicTermJpaEntity> terms = new ArrayList<>(page.getContent());

        terms.sort(Comparator.comparing(AcademicTermJpaEntity::getStartDate));

        List<AcademicTerm> content = IntStream.range(0, terms.size())
                .mapToObj(i -> mapper.toDomain(terms.get(i), i + 1))
                .toList();

        return new PageImpl<>(content, pageable, page.getTotalElements());
    }
}
