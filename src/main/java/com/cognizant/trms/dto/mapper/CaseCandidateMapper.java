/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.opportunity.CaseCandidateDto;

import com.cognizant.trms.model.opportunity.CaseCandidate;

/**
 * @author Vara Kotha
 *
 */
public class CaseCandidateMapper {

	public static CaseCandidateDto toCaseCandidateDto(CaseCandidate caseCandidate) {
		return new CaseCandidateDto().setId(caseCandidate.getId()).setOnBoarded(caseCandidate.isOnBoarded())
				.setSoCaseId(caseCandidate.getSoCaseId()).setSoMappedCandidateId(caseCandidate.getSoMappedCandidateId())
				.setStatus(caseCandidate.getStatus());

		// TODO: Set Interview List
	}

}
