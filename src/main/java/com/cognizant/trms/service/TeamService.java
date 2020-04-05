/**
 * 
 */
package com.cognizant.trms.service;

import java.util.List;
import java.util.Set;

import com.cognizant.trms.controller.v1.request.TeamCreateRequest;
import com.cognizant.trms.dto.model.user.TeamDto;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Vara Kotha
 *
 */
public interface TeamService {

	Set<TeamDto> listTeams();

	TeamDto createTeam(TeamCreateRequest teamCreateRequest) throws JsonProcessingException;

	TeamDto updateTeam(TeamCreateRequest teamCreateRequest) throws JsonProcessingException;

	Boolean deleteTeam(String id);

	List<TeamDto> getTeamsByProgramId(String Id) throws JsonProcessingException;

	TeamDto getTeamsById(String Id);

}
