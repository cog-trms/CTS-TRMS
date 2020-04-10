package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.MapCandidateToCase;
import com.cognizant.trms.controller.v1.request.MapCandidateToInterview;
import com.cognizant.trms.controller.v1.request.MapCandidateToSo;
import com.cognizant.trms.controller.v1.request.SOCreateRequest;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.SOService;
import com.cognizant.trms.util.TRMSUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
@RestController
@RequestMapping("/api/v1/sorders")
@Api(value = "trms-application", description = "Operations pertaining to SO management in the TRMS application")
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

    @PostMapping(path = "/candidate", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response mapCandidateToSO(@Valid @RequestBody MapCandidateToSo mapCandidateToSo) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(soService.addCandidateToSo(mapCandidateToSo));

    }
    @PostMapping(path = "/candidate/case", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response mapCandidateToCase(@Valid @RequestBody MapCandidateToCase mapCandidateToCase) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(soService.addCandidateToCase(mapCandidateToCase));

    }

    @PostMapping(path = "/candidate/interview", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response mapCandidateToInterview(@Valid @RequestBody MapCandidateToInterview mapCandidateToInterview) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(soService.addCandidateToInterview(mapCandidateToInterview));

    }

// TODO - VARA START GET CASE-CANDIDATES of a CASE
    @GetMapping(path = "/so/case/{caseId}/candidate",  produces = "application/json")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getCaseCandidate(@PathVariable("caseId") String caseId) throws JsonProcessingException {
        return Response
                .ok();
                //.setPayload(soService.addCandidateToSo(mapCandidateToSo));

    }
    // TODO - END


    // TODO - VARA START GET CASES OF A SO
    @GetMapping(path = "/so/{soId}/cases",  produces = "application/json")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getCases(@PathVariable("soId") String soId) throws JsonProcessingException {
        return Response
                .ok();
        //.setPayload(soService.addCandidateToSo(mapCandidateToSo));

    }
    // TODO - END

    // TODO - VARA START GET SO of a Logged in User
    @GetMapping(path = "/so",  produces = "application/json")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getSo() throws JsonProcessingException {
        return Response
                .ok();
        //.setPayload(soService.getSOByLoginUser());

    }
    // TODO - END


    //TODO - List the SO's based on Account?
    //TODO - TAG model designing  -- ARAVIND

}
