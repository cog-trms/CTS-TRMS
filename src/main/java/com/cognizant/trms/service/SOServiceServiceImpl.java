package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.MapCandidateToSo;
import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.mapper.SOMapper;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.opportunity.Candidate;
import com.cognizant.trms.model.opportunity.SO;
import com.cognizant.trms.model.opportunity.SOCandidate;
import com.cognizant.trms.model.opportunity.SOCase;
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
                SOCandidate soCandidate = new SOCandidate()
                        .setCandidate(candidate.get())
                        .setActive(mapCandidateToSo.isActive())
                        .setSoId(mapCandidateToSo.getSoId());
                soCandidateRepository.save(soCandidate);

                //Updating the SO_CANDIDATE ref in SO collection
                if(existingSo.get().getCandidates() == null) {
                    existingSo.get().setCandidates(new ArrayList<>());
                }
                    existingSo.get().getCandidates().add(soCandidate);
                return SOMapper.toSODtoNoCase(soRepository.save(existingSo.get()));
                //return modelMapper.map( soRepository.save(existingSo.get()), SODto.class);
            }
            throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, candidateId + " is not found");
        }
        throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.BAD_REQUEST, soId + " not found");
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
