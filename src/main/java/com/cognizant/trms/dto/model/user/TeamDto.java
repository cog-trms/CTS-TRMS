package com.cognizant.trms.dto.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDto implements Comparable {

	private String id;
	private String teamName;
    private AccountDto account;
    private UserDto programManager;
	private ProgramDto program;
	private Set<UserDto> teamMembers;

	@Override
	public int compareTo(Object arg0) {
		return this.getTeamName().compareTo(((TeamDto) arg0).getTeamName());
	}

}
