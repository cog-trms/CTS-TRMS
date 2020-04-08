/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.cognizant.trms.dto.model.user.TeamDto;
import com.cognizant.trms.dto.model.user.UserDto;

import com.cognizant.trms.model.user.Team;

/**
 * @author Vara Kotha
 *
 */
@Component
public class TeamMapper {

	public static TeamDto toTeamDto(Team team) {
		return new TeamDto().setId(team.getId()).setTeamName(team.getTeamName())
				.setProgram(ProgramMapper.toProgramDto(team.getProgram()))
				.setTeamMembers(new HashSet<UserDto>(Optional.ofNullable(team.getTeamMembers())
						.orElse(Collections.emptySet()).stream().filter(Objects::nonNull)
						.map(user -> new ModelMapper().map(user, UserDto.class)).collect(Collectors.toSet())));

	}

}
