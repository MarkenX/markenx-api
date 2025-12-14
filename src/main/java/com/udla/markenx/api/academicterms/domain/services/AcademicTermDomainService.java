package com.udla.markenx.api.academicterms.domain.services;

import com.udla.markenx.api.academicterms.domain.models.aggregates.AcademicTerm;

import java.util.List;

public class AcademicTermDomainService {

    private AcademicTermDomainService() {}

    /**
     * Calculates the position of the given academic term in the list of academic terms.
     * If the list is empty, the sequence starts at 1. If the academic term does not exist
     * in the list, it is treated as the next term in the sequence.
     *
     * @param academicTerms the list of academic terms to be checked
     * @param academicTerm the specific academic term whose sequence position is to be determined
     * @return the sequence number of the academic term in the list, starting from 1
     */
    public static int calculateSequence(List<AcademicTerm> academicTerms, AcademicTerm academicTerm) {
        if (academicTerms.isEmpty()) return 1;
        if (!academicTerms.contains(academicTerm)) {
            return academicTerms.size() + 1;
        }
        return academicTerms.indexOf(academicTerm) + 1;
    }
}
