package com.udla.markenx.api.academicterms.infrastructure.persistance.jpa;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;
import com.udla.markenx.api.academicterms.domain.models.valueobjects.AcademicTermStatus;
import com.udla.markenx.api.academicterms.domain.ports.outgoing.AcademicTermRepository;
import com.udla.markenx.api.academicterms.infrastructure.mappers.AcademicTermJpaMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
        return mapper.toDomain(saved);
    }

    /**
     * Finds an academic term by its unique identifier.
     *
     * @param id the unique identifier of the academic term
     * @return an {@code Optional} containing the academic term in its domain representation
     *         if found, or an empty {@code Optional} if not found
     */
    @Override
    public Optional<AcademicTerm> findById(String id) {
        Optional<AcademicTermJpaEntity> entity = springRepo.findById(id);
        return entity.map(mapper::toDomain);
    }

    /**
     * Retrieves all academic terms from the data source, maps them to their domain representations,
     * and returns them as a list.
     *
     * @return a list of academic terms in their domain representation
     */
    @Override
    public List<AcademicTerm> findAll() {
        return springRepo.findAll().stream().map(mapper::toDomain).toList();
    }

    /**
     * Retrieves a list of academic terms for the specified year, maps them to their domain
     * representations, and returns them as a list.
     *
     * @param year the year for which academic terms need to be retrieved
     * @return a list of academic terms in their domain representation for the specified year
     */
    @Override
    public List<AcademicTerm> findAllByYear(int year) {
        return springRepo.findByYear(year).stream().map(mapper::toDomain).toList();
    }

    /**
     * Retrieves a list of academic terms whose status does not match the specified status,
     * maps them to their domain representations, and returns them as a list.
     *
     * @param status the status to be excluded from the search criteria
     * @return a list of academic terms in their domain representation where the status does not match the specified value
     */
    @Override
    public List<AcademicTerm> findByStatusNot(AcademicTermStatus status) {
        return springRepo.findByStatusNot(status).stream().map(mapper::toDomain).toList();
    }

    /**
     * Retrieves a paginated list of academic terms from the data source, maps them to their domain
     * representations, and returns them as a page of results.
     *
     * @param pageable the pagination and sorting information
     * @return a page of academic terms in their domain representation
     */
    @Override
    public Page<@NotNull AcademicTerm> findAllPaginated(Pageable pageable) {
        return springRepo.findAll(pageable).map(mapper::toDomain);
    }
}
