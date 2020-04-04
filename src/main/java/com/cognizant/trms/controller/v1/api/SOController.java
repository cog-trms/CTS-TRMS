package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.SOService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/*
    Author: Aravindan Dandapani
*/
@RestController
@RequestMapping("/api/v1/sorders")
@Api(value="trms-application", description="Operations pertaining to SO management in the TRMS application")
public class SOController {
    private static final Logger log = LogManager.getLogger(SOController.class);
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SOService soService;

    @PostMapping("/")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response createSO(@RequestBody @Valid SOCreateRequest soCreateRequest) throws JsonProcessingException {
        String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(soCreateRequest);
        log.debug("Controler - SO Creation Request object " + reqString);
        return Response
                .ok()
                .setPayload(soService.createSO(soCreateRequest));
    }
}
