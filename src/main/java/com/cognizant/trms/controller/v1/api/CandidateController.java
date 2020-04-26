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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


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
    public Response getAllCandidates() throws JsonProcessingException {

        //This should be restricted only to TAG
        return Response
                .ok()
                .setPayload(candidateService.getAllCandidates());
    }
    
    @GetMapping("/candidate/candidateId/{candidateId}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getCandidateById(@PathVariable("candidateId") String candidateId) throws JsonProcessingException {

        return Response
                .ok()
                .setPayload(candidateService.getCandidateById(candidateId));
    }
    
    @GetMapping("/candidate/candidateEmail/{candidateEmail}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getCandidateByEmail(@PathVariable("candidateEmail") String candidateEmail) throws JsonProcessingException {

        return Response
                .ok()
                .setPayload(candidateService.getCandidateByEmail(candidateEmail));
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

    /**
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/candidate/{id}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	public Response deleteProgram(@PathVariable("id") String id) {
		return Response.ok().setPayload(candidateService.deleteCandidateById(id));
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

