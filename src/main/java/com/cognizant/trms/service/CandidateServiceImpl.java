package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.CandidateCreateRequest;
import com.cognizant.trms.dto.model.opportunity.CandidateDto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.opportunity.Candidate;
import com.cognizant.trms.repository.user.CandidateRepository;
import com.cognizant.trms.util.TRMSUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
@Component
public class CandidateServiceImpl implements CandidateService {
    private static final Logger log = LogManager.getLogger(CandidateServiceImpl.class);

    @Autowired
    CandidateRepository candidateRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<CandidateDto> getAllCandidates() {
        return candidateRepository.findByIsActive(true)
                .stream()
                .filter(Objects::nonNull)
                .map(candidate -> modelMapper.map(candidate, CandidateDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CandidateDto createCandidate(CandidateCreateRequest candidateCreateRequest) {
        Candidate candidate = new Candidate()
                .setCandidateEmail(candidateCreateRequest.getCandidateEmail())
                .setActive(candidateCreateRequest.isActive())
                .setFirstName(candidateCreateRequest.getFirstName())
                .setLastName(candidateCreateRequest.getLastName())
                .setMobile(candidateCreateRequest.getMobile());
        return modelMapper.map( candidateRepository.save(candidate), CandidateDto.class);
    }

    @Override
    public CandidateDto updateCandidate(CandidateCreateRequest candidateCreateRequest) {
        String candidateId = candidateCreateRequest.getId();
       Optional<Candidate> existingCandidate = candidateRepository.findById(candidateId);

       if(existingCandidate.isPresent()) {
           Candidate candidate = existingCandidate.get();
            candidate
                   .setCandidateEmail(candidateCreateRequest.getCandidateEmail())
                   .setActive(candidateCreateRequest.isActive())
                   .setFirstName(candidateCreateRequest.getFirstName())
                   .setLastName(candidateCreateRequest.getLastName())
                   .setMobile(candidateCreateRequest.getMobile());
            return modelMapper.map(candidateRepository.save(candidate),CandidateDto.class);
       }

        throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, candidateId);
    }

    @Override
    public CandidateDto getCandidateById(String id) {
    	Optional<Candidate> candidate = candidateRepository.findById(id);
    	if(candidate.isPresent()) {
    		return modelMapper.map(candidate.get(),CandidateDto.class);
    		
    	}throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, id);
    }

    @Override
    public CandidateDto getCandidateByEmail(String email) {
    	Candidate candidate = candidateRepository.findByCandidateEmail(email);
    	if(candidate!=null) {
    		return modelMapper.map(candidate,CandidateDto.class);
    	}throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, email);
    }

    @Override
    public boolean deleteCandidateById(String id) {
		if (TRMSUtil.isAdmin()) {
			Optional<Candidate> candidate = candidateRepository.findById(id);
			if (candidate.isPresent()) {
				candidateRepository.deleteById(id);
				return true;
			}
			throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, id);
		}
		throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.ACCESS_DENIED,
				" Only an admin user can perform this operation");
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
