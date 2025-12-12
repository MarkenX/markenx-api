package com.udla.markenx.api.academicterms.infrastructure.persistance.jpa;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermRepository;
import com.udla.markenx.api.academicterms.infrastructure.mappers.AcademicTermJpaMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaAcademicTermRepository implements AcademicTermRepository {

    private final SpringDataAcademicTermRepository springRepo;
    private final AcademicTermJpaMapper mapper;

    public JpaAcademicTermRepository(SpringDataAcademicTermRepository springRepo,
                                     AcademicTermJpaMapper mapper) {
        this.springRepo = springRepo;
        this.mapper = mapper;
    }

    @Override
    public AcademicTerm save(AcademicTerm newAcademicTerm) {
        AcademicTermJpaEntity saved = springRepo.save(mapper.toEntity(newAcademicTerm));
        return mapper.toDomain(saved);
    }

    @Override
    public Page<@NotNull AcademicTerm> findAllPaginated(Pageable pageable) {
        return springRepo.findAll(pageable).map(mapper::toDomain);
    }
}
