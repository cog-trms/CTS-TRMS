package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.controller.v1.request.CaseCreateRequest;
import com.cognizant.trms.dto.model.opportunity.SOCandidateDto;
import com.cognizant.trms.dto.model.opportunity.SOCaseDto;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.cognizant.trms.model.opportunity.SO;
import com.cognizant.trms.model.opportunity.SOCandidate;
import com.cognizant.trms.model.opportunity.SOCase;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
public class SOMapper {

	public static SOCase toCaseReqToCase(CaseCreateRequest caseCreateRequest, String soId) {

		return new SOCase().setLevel(caseCreateRequest.getLevel()).setSkill(caseCreateRequest.getSkill()).setSoId(soId)
				.setNumberOfPosition(caseCreateRequest.getNumberOfPosition());

	}

	public static SODto toSODto(SO so, List<SOCase> cases) {
		List<SOCandidate> soCandidateList = Optional.ofNullable(so.getCandidates()).orElse(Collections.emptyList());

		return new SODto()
				.setCases(cases.stream().map(caseM -> new ModelMapper().map(caseM, SOCaseDto.class))
						.collect(Collectors.toList()))
				.setId(so.getId()).setLocation(so.getLocation()).setPositionCount(so.getPositionCount())
				.setServiceOrder(so.getServiceOrder()).setTeamId(so.getTeamId())
				.setSoCandidates(soCandidateList.stream().filter(soCandidate -> soCandidate != null)
						.map(soCandidate -> new ModelMapper().map(soCandidate, SOCandidateDto.class))
						.collect(Collectors.toList()));
	}

	public static SODto toSODtoNoCase(SO so) {
		List<SOCandidate> soCandidateList = Optional.ofNullable(so.getCandidates()).orElse(Collections.emptyList());

		return new SODto().setId(so.getId()).setLocation(so.getLocation()).setPositionCount(so.getPositionCount())
				.setServiceOrder(so.getServiceOrder()).setTeamId(so.getTeamId()).setCreateUser(so.getCreateUser())
				.setCreatedDate(so.getCreatedDate()).setLastModifiedUser(so.getLastModifiedUser())
				.setUpdateDate(so.getUpdateDate())
				.setSoCandidates(soCandidateList.stream().filter(soCandidate -> soCandidate != null)
						.map(soCandidate -> new ModelMapper().map(soCandidate, SOCandidateDto.class))
						.collect(Collectors.toList()));
	}
}
