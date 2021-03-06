package com.cognizant.trms.service;

/*
    Author: Aravindan Dandapani
*/

import com.cognizant.trms.controller.v1.request.*;
import com.cognizant.trms.dto.model.opportunity.CaseCandidateDto;
import com.cognizant.trms.dto.model.opportunity.InterviewDto;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Set;

public interface SOService {
	SODto createSO(SOCreateRequest soCreateRequest) throws JsonProcessingException;

	Set<SODto> getSOByLoginUser() throws JsonProcessingException;

	SODto getCasesBySO(String soId) throws JsonProcessingException;

	List<CaseCandidateDto> getCaseCandidateByCaseId(String caseId) throws JsonProcessingException;

	SODto addCandidateToSo(MapCandidateToSo mapCandidateToSo);

	CaseCandidateDto addCandidateToCase(MapCandidateToCase mapCandidateToCase);

	InterviewDto addCandidateToInterview(MapCandidateToInterview mapCandidateToInterview);

	CaseCandidateDto updateCandidateStatus(CaseCandidateStatusUpdateRequest caseCandidateStatusUpdateRequest);

	boolean deleteCaseCandidate(String caseCandidateId);

	CaseCandidateDto onboardCaseCandidate(String caseCandidateId);

	List<InterviewDto> getInterviewsByPanelUserId(String panelUserId) throws JsonProcessingException;
}
