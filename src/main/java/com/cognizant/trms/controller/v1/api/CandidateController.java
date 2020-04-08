package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.CandidateCreateRequest;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.service.CandidateService;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/*
    Author: Aravindan Dandapani
*/
@RestController
@RequestMapping("/api/v1/candidates")
@Api(value="trms-application", description="Operations pertaining to Candidate management in the TRMS application")
public class CandidateController {
    private static final Logger log = LogManager.getLogger(CandidateController.class);

    @Autowired
    CandidateService candidateService;
    @Autowired
    ObjectMapper objectMapper;

    @GetMapping("/candidate/all")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAllAccount() throws JsonProcessingException {

        //This should be restricted only to TAG
        return Response
                .ok()
                .setPayload(candidateService.getAllCandidates());
    }

    @PostMapping("/candidate")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response createCandidate( @Valid @RequestBody  CandidateCreateRequest candidateCreateRequest)
            throws JsonProcessingException, MethodArgumentNotValidException {

        log.debug("Inside Candidate Controller");
        log.debug("Request" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(candidateCreateRequest));

        return Response
                .ok()
                .setPayload(candidateService.createCandidate(candidateCreateRequest));
    }




    @PutMapping("/candidate")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response updateCandidate(@RequestBody @Valid CandidateCreateRequest candidateCreateRequest){
        return Response
                .ok()
                .setPayload(candidateService.updateCandidate(candidateCreateRequest));
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return TRMSException.throwException(entityType, exceptionType, args);
    }

    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }

}

