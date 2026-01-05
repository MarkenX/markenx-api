package com.udla.markenx.api.classroom.academicterms.domain.services;

import com.udla.markenx.api.classroom.academicterms.domain.exceptions.TermsOverlapException;
import com.udla.markenx.api.classroom.academicterms.domain.models.aggregates.AcademicTerm;

import java.util.Collection;
import java.util.List;

public class AcademicTermDomainService {

    private AcademicTermDomainService() {}

    /**
     * Calculates the sequence number of the specified academic term within the list of academic terms.
     * If the academic term is not found in the list or is null, the sequence number is calculated
     * as the size of the list plus one. If the list is empty, the sequence number starts at 1.
     *
     * @param terms the list of academic terms to search in
     * @param term the academic term to find the sequence number for
     * @return the sequence number of the academic term, or, if the academic term is not found or is null,
     *         the sequence number as the size of the list plus one
     */
    public static int calculateSequence(List<AcademicTerm> terms, AcademicTerm term) {
        if (terms.isEmpty()) return 1;
        if (!terms.contains(term) || term == null) {
            return terms.size() + 1;
        }
        return terms.indexOf(term) + 1;
    }

    /**
     * Validates that the provided academic term does not overlap with any of the existing terms
     * in the given collection. If an overlap is detected, a {@link TermsOverlapException} is thrown.
     *
     * @param terms the collection of existing academic terms to validate against
     * @param term the academic term to validate for overlaps
     * @throws TermsOverlapException if the provided term overlaps with any term in the collection
     */
    public static void validateNoOverlaps(Collection<AcademicTerm> terms, AcademicTerm term) {
        terms.forEach(t -> {
            if (t.overlapsWith(term)) {
                throw new TermsOverlapException(t, term);
            }
        });
    }
}
