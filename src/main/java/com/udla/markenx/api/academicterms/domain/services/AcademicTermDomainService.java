package com.udla.markenx.api.academicterms.domain.services;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;

import java.util.List;

public class AcademicTermDomainService {

    private AcademicTermDomainService() {}

    /**
     * Calculates the sequence number of the specified academic term within the list of academic terms.
     * If the academic term is not found in the list or is null, the sequence number is calculated
     * as the size of the list plus one. If the list is empty, the sequence number starts at 1.
     *
     * @param academicTerms the list of academic terms to search in
     * @param academicTerm the academic term to find the sequence number for
     * @return the sequence number of the academic term, or, if the academic term is not found or is null,
     *         the sequence number as the size of the list plus one
     */
    public static int calculateSequence(List<AcademicTerm> academicTerms, AcademicTerm academicTerm) {
        if (academicTerms.isEmpty()) return 1;
        if (!academicTerms.contains(academicTerm) || academicTerm == null) {
            return academicTerms.size() + 1;
        }
        return academicTerms.indexOf(academicTerm) + 1;
    }
}
