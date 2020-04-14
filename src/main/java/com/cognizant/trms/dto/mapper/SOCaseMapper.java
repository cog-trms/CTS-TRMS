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
import com.cognizant.trms.dto.model.opportunity.SOCaseDto;
import com.cognizant.trms.model.opportunity.CaseCandidate;
import com.cognizant.trms.model.opportunity.SOCase;

/**
 * @author Vara Kotha
 *
 */
public class SOCaseMapper {
	public static SOCaseDto toSOCaseDto(SOCase soCase) {
		List<CaseCandidate> caseCandidateList = Optional.ofNullable(soCase.getCaseCandidates())
				.orElse(Collections.emptyList());

		return new SOCaseDto().setId(soCase.getId()).setSoId(soCase.getSoId()).setLevel(soCase.getLevel())
				.setSkill(soCase.getSkill()).setNumberOfPosition(soCase.getNumberOfPosition())
				.setNumberOfFilled(soCase.getNumberOfFilled()).setNumberOfSelected(soCase.getNumberOfSelected())
				.setStatus("").setNumberOfSelected(0).setNumberOfFilled(0)
				.setCaseCandidates(caseCandidateList.stream().filter(caseCandidate -> caseCandidate != null)
						.map(caseCandidate -> new ModelMapper().map(caseCandidate, CaseCandidateDto.class))
						.collect(Collectors.toList()));

	}
}
