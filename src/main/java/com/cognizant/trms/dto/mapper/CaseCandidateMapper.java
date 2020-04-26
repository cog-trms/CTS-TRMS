/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import com.cognizant.trms.dto.model.opportunity.CaseCandidateDto;
import com.cognizant.trms.dto.model.opportunity.InterviewDto;
import com.cognizant.trms.model.opportunity.CaseCandidate;
import com.cognizant.trms.model.opportunity.Interview;

/**
 * @author Vara Kotha
 *
 */
public class CaseCandidateMapper {

	public static CaseCandidateDto toCaseCandidateDto(CaseCandidate caseCandidate) {
		
		List<Interview> interviewList = Optional.ofNullable(caseCandidate.getInterviews())
				.orElse(Collections.emptyList());
		
		return new CaseCandidateDto().setId(caseCandidate.getId()).setOnBoarded(caseCandidate.isOnBoarded())
				.setSoCaseId(caseCandidate.getSoCaseId()).setSoMappedCandidateId(caseCandidate.getCandidateId())
				.setStatus(caseCandidate.getStatus())
				.setInterviews(interviewList.stream().filter(interview -> interview != null)
						.map(interview -> new ModelMapper().map(interview,InterviewDto.class))
						.collect(Collectors.toList()));
	}

}
