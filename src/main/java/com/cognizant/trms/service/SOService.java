package com.cognizant.trms.service;

/*
    Author: Aravindan Dandapani
*/

import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface SOService {
    SODto createSO(SOCreateRequest soCreateRequest) throws JsonProcessingException;
    List<SODto> getSOByLoginUser();
}
