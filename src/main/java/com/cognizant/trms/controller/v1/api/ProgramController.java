/**
 * 
 */
package com.cognizant.trms.controller.v1.api;

import java.util.Set;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.trms.controller.v1.request.ProgramCreationRequest;
import com.cognizant.trms.controller.v1.request.ProgramUpateRequest;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.ProgramService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Vara Kotha
 *
 */

@RestController
@RequestMapping("/api/v1/programs")
@Api(value = "trms-application", description = "Operations pertaining to program management in the TRMS application")
public class ProgramController {

	private static final Logger log = LogManager.getLogger(ProgramController.class);

	@Autowired
	private ProgramService programService;

	/***
	 * Retrieve all programs
	 * 
	 * @return
	 */
	@GetMapping("/all")
	@ApiOperation(value = "API handler for getting All Program details", authorizations = {
			@Authorization(value = "apiKey") })
	public Response getProgramDetails() {
		log.debug("Inside getAllPrograms API Method");
		Set<ProgramDto> programSet = programService.listPrograms();
		return Response.ok().setPayload((Set<ProgramDto>) programSet);
	}

	/***
	 *  
	 * @param programCreationRequest
	 * @return
	 */
	@PostMapping("/program")
	@ApiOperation(value = "API handler for creating new program details", authorizations = {
			@Authorization(value = "apiKey") })
	public ProgramDto createProgram(@Valid @RequestBody ProgramCreationRequest programCreationRequest) {
		log.debug("Inside createProgram API Method");

		ProgramDto programDto = new ProgramDto();
		programDto.setProgramName(programCreationRequest.getName());
		programDto.setAccountId(programCreationRequest.getAccountId());
		programDto.setUserId(programCreationRequest.getUserId());

		return programService.createProgram(programDto);
	}

	/***
	 *  
	 * @param ProgramUpateRequest
	 * @return
	 */
	@PatchMapping("/program")
	@ApiOperation(value = "API handler for updating program details", authorizations = {
			@Authorization(value = "apiKey") })
	public ProgramDto updateProgram(@Valid @RequestBody ProgramUpateRequest programUpdateRequest) {
		log.debug("Inside updateProgram API Method");

		ProgramDto programDto = new ProgramDto();
		programDto.setProgramName(programUpdateRequest.getName());
		programDto.setAccountId(programUpdateRequest.getAccountId());
		programDto.setUserId(programUpdateRequest.getUserId());
		programDto.setProgramId(programUpdateRequest.getProgramId());

		return programService.updateProgram(programDto);
	}

	/***
	 * Delete Program Input: ProgramName, AccountId and UserId
	 * 
	 * @param ProgramUpateRequest
	 * @return
	 */
	@DeleteMapping("/program/{id}")
	@ApiOperation(value = "", authorizations = { @Authorization(value = "apiKey") })
	public Response deleteProgram(@PathVariable("id") String id) {
		return Response.ok().setPayload(programService.deleteProgram(id));
	}

}
