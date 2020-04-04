package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.CaseCreateRequest;
import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.mapper.SOMapper;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.opportunity.SO;
import com.cognizant.trms.model.opportunity.SOCase;
import com.cognizant.trms.repository.opportunity.SOCaseRepository;
import com.cognizant.trms.repository.opportunity.SORepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
