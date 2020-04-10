/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.opportunity.SOCaseDto;

import com.cognizant.trms.model.opportunity.SOCase;

/**
 * @author Vara Kotha
 *
 */
public class SOCaseMapper {
	public static SOCaseDto toSOCaseDto(SOCase soCase) {
		return new SOCaseDto().setId(soCase.getId()).setSoId(soCase.getSoId()).setLevel(soCase.getLevel())
				.setSkill(soCase.getSkill()).setNumberOfPosition(soCase.getNumberOfPosition())
				.setNumberOfFilled(soCase.getNumberOfFilled()).setNumberOfSelected(soCase.getNumberOfSelected());

		// TODO: Case list
	}
}
