/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.user.TeamDto;

import com.cognizant.trms.model.user.Team;

/**
 * @author Vara Kotha
 *
 */
public class TeamMapper {

	public static TeamDto toTeamDto(Team team) {
		return new TeamDto().setId(team.getId()).setTeamName(team.getTeamName())
				.setProgram(ProgramMapper.toProgramDto(team.getProgram()));

	}

}
