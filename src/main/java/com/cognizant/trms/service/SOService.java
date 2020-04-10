package com.cognizant.trms.service;

/*
    Author: Aravindan Dandapani
*/

import com.cognizant.trms.controller.v1.request.MapCandidateToCase;
import com.cognizant.trms.controller.v1.request.MapCandidateToInterview;
import com.cognizant.trms.controller.v1.request.MapCandidateToSo;
import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.model.opportunity.CaseCandidateDto;
import com.cognizant.trms.dto.model.opportunity.InterviewDto;
import com.cognizant.trms.dto.model.opportunity.SOCaseDto;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface SOService {
	SODto createSO(SOCreateRequest soCreateRequest) throws JsonProcessingException;

	List<SODto> getSOByLoginUser() throws JsonProcessingException;

	List<SOCaseDto> getCasesBySO(String soId) throws JsonProcessingException;

	List<CaseCandidateDto> getCaseCandidateByCaseId(String caseId) throws JsonProcessingException;

	SODto addCandidateToSo(MapCandidateToSo mapCandidateToSo);

	CaseCandidateDto addCandidateToCase(MapCandidateToCase mapCandidateToCase);

	InterviewDto addCandidateToInterview(MapCandidateToInterview mapCandidateToInterview);
}
