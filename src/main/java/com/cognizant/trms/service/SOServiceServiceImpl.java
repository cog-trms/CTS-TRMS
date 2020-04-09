package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.MapCandidateToCase;
import com.cognizant.trms.controller.v1.request.MapCandidateToSo;
import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.mapper.SOMapper;
import com.cognizant.trms.dto.model.opportunity.CaseCandidateDto;
import com.cognizant.trms.dto.model.opportunity.SOCaseDto;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.opportunity.*;
import com.cognizant.trms.repository.opportunity.CaseCandidateRepository;
import com.cognizant.trms.repository.opportunity.SOCandidateRepository;
import com.cognizant.trms.repository.opportunity.SOCaseRepository;
import com.cognizant.trms.repository.opportunity.SORepository;
import com.cognizant.trms.repository.user.CandidateRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.ObjPtr;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
@Component
public class SOServiceServiceImpl implements SOService {
    private static final Logger log = LogManager.getLogger(SOServiceServiceImpl.class);

    @Autowired
    SORepository soRepository;

    @Autowired
    SOCaseRepository soCaseRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SOCandidateRepository soCandidateRepository;

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CaseCandidateRepository caseCandidateRepository;



    @Override
    public SODto createSO(SOCreateRequest soCreateRequest) throws JsonProcessingException {
        SO newSO = soRepository.findByServiceOrder(soCreateRequest.getServiceOrder());
        if (newSO == null) {
            newSO = new SO()
                    .setServiceOrder(soCreateRequest.getServiceOrder())
                    .setLocation(soCreateRequest.getLocation())
                    .setCreatedBy(soCreateRequest.getCreatedBy())
                    .setPositionCount(soCreateRequest.getPositionCount())
                    .setTeamId(soCreateRequest.getTeamId());
            String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newSO);
            log.debug("SO Request  " + reqString);

            SO createdSO = soRepository.save(newSO);
            log.debug("SO Response" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createdSO));
            String soId = createdSO.getId();
            List<SOCase> cases = null;
            if (!soCreateRequest.getCases().isEmpty()) {
                cases = soCreateRequest.getCases()
                        .stream()
                        .map(item -> SOMapper.toCaseReqToCase(item, soId))
                        .collect(Collectors.toList());
                log.debug("Case Request" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cases));

                cases = soCaseRepository.saveAll(cases);
                log.debug("Case Response" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cases));

            }
            return SOMapper.toSODto(createdSO, cases);
        }
        throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.DUPLICATE_ENTITY, soCreateRequest.getServiceOrder());
    }

    @Override
    public List<SODto> getSOByLoginUser() {
        return null;
    }

    @Override
    public SODto addCandidateToSo(MapCandidateToSo mapCandidateToSo) {
        String candidateId = mapCandidateToSo.getCandidateId();
        String soId = mapCandidateToSo.getSoId();
        Optional<SO> existingSo = soRepository.findById(soId);
        if (existingSo.isPresent()) {
            Optional<Candidate> candidate = candidateRepository.findById(candidateId);
            if (candidate.isPresent()) {
                if (candidate.get().isActive()) {
                    SOCandidate soCandidate = new SOCandidate()
                            .setCandId(mapCandidateToSo.getCandidateId())
                            .setCandidate(candidate.get())
                            .setActive(mapCandidateToSo.isActive())
                            .setSoId(mapCandidateToSo.getSoId());
                    soCandidateRepository.save(soCandidate);

                    //Updating the SO_CANDIDATE ref in SO collection
                    if (existingSo.get().getCandidates() == null) {
                        existingSo.get().setCandidates(new ArrayList<>());
                    }
                    existingSo.get().getCandidates().add(soCandidate);
                    return SOMapper.toSODtoNoCase(soRepository.save(existingSo.get()));
                    //return modelMapper.map( soRepository.save(existingSo.get()), SODto.class);
                }
                throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, candidateId + " not active");
            }
            throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, candidateId + " is not found");
        }
        throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.BAD_REQUEST, soId + " not found");
    }

    @Override
    public CaseCandidateDto addCandidateToCase(MapCandidateToCase mapCandidateToCase) {
        String candidateId = mapCandidateToCase.getSoMappedCandidateId();
        String caseId = mapCandidateToCase.getSoCaseId();
        Optional<SOCase> existingCase = soCaseRepository.findById(caseId);
        if (existingCase.isPresent()) {
            SOCandidate soCandidate = soCandidateRepository.findByCandidateId(candidateId);
            if (soCandidate != null) {
                if (soCandidate.isActive()) {
                    CaseCandidate caseCandidate = new CaseCandidate()
                            .setSoCaseId(caseId)
                            .setSoMappedCandidateId(candidateId)
                            .setStatus(CASE_CANDIDATE_STATUS.MAPPED.getValue()); // Setting the initial value
                    return modelMapper.map(caseCandidateRepository.save(caseCandidate), CaseCandidateDto.class);
                }
                throw exceptionWithId(EntityType.SO_CANDIDATE, ExceptionType.BAD_REQUEST, "Candidate id "+candidateId + " is not active in SO " + soCandidate.getSoId());
            }
            throw exceptionWithId(EntityType.SO_CANDIDATE, ExceptionType.BAD_REQUEST, "Candidate id "+candidateId + " is not found in SO");
        }
        throw exceptionWithId(EntityType.CASE, ExceptionType.BAD_REQUEST, caseId + " is not found");
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }

    public enum CASE_CANDIDATE_STATUS {
        MAPPED("MAPPED"),
        SCHEDULED("SCHEDULED"),
        INTERVIEW_SLOT_REQUESTED("INTERVIEW_SLOT_REQUESTED");

        String value;

        CASE_CANDIDATE_STATUS(String value) {
            this.value = value;
        }

        String getValue() {
            return this.value;
        }
    }
}
