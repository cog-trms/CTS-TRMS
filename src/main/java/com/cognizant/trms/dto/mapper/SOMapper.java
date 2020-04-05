package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.controller.v1.request.CaseCreateRequest;
import com.cognizant.trms.dto.model.opportunity.SOCaseDto;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.cognizant.trms.model.opportunity.SO;
import com.cognizant.trms.model.opportunity.SOCase;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
public class SOMapper {

    public static SOCase toCaseReqToCase(CaseCreateRequest caseCreateRequest, String soId){

        return new SOCase()
                .setLevel(caseCreateRequest.getLevel())
                .setSkill(caseCreateRequest.getSkill())
                .setSoId(soId)
                .setNumberOfPosition(caseCreateRequest.getNumberOfPosition());

    }

    public static SODto toSODto(SO so, List<SOCase> cases){

        return new SODto()
                .setCases(cases
                        .stream()
                        .map(caseM -> new ModelMapper().map(caseM, SOCaseDto.class))
                        .collect(Collectors.toList()))
                .setCreatedBy(so.getCreatedBy())
                .setId(so.getId())
                .setLocation(so.getLocation())
                .setPositionCount(so.getPositionCount())
                .setServiceOrder(so.getServiceOrder())
                .setTeamId(so.getTeamId());
    }
}
