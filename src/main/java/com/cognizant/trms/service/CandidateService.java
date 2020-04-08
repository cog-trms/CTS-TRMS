package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.CandidateCreateRequest;
import com.cognizant.trms.dto.model.opportunity.CandidateDto;

import java.util.List;

public interface CandidateService {

    List<CandidateDto> getAllCandidates();
    CandidateDto createCandidate(CandidateCreateRequest candidateCreateRequest);
    CandidateDto updateCandidate(CandidateCreateRequest candidateCreateRequest);
    CandidateDto getCandidateById(String id);
    CandidateDto getCandidateByEmail(String email);
    boolean deleteCandidateById(String id);
}
