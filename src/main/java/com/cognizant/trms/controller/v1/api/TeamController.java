/**
 * 
 */
package com.cognizant.trms.controller.v1.api;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.trms.controller.v1.request.TeamCreateRequest;
import com.cognizant.trms.dto.model.user.TeamDto;
import com.cognizant.trms.service.TeamService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

/**
 * @author Vara Kotha
 *
 */

@RestController
@RequestMapping("/api/v1/teams")
@Api(value = "trms-application", description = "Operations pertaining to Team management in the TRMS application")
public class TeamController {

	private static final Logger log = LogManager.getLogger(TeamController.class);

	@Autowired
	private TeamService teamService;

	/***
	 * 
	 * @param programCreationRequest
	 * @return
	 */
	@PostMapping("/team")
	@ApiOperation(value = "API handler for creating new team details", authorizations = {
			@Authorization(value = "apiKey") })
	public TeamDto createTeam(@Valid @RequestBody TeamCreateRequest teamCreateRequest) throws JsonProcessingException {
		log.debug("Inside createTeam API Method");
		return teamService.createTeam(teamCreateRequest);
	}

}
