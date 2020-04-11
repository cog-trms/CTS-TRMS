
package com.cognizant.trms.dto.model.user;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

// VARA TODO: Have to add different flavor of ProgramDto constructors

public class ProgramDto implements Comparable{
    private String id;
    private String programName;
    private String accountId;
    private String accountName;
    private String programMgrId;
    private String programMgrFirstName;
    private String programMgrLastName;
    private String programMgrEmail;
    private AccountDto account;
    private UserDto programManager;
    private List<TeamDto> teamList;
    

	@Override
	public int compareTo(Object o) {
		return this.getProgramName().compareTo(((ProgramDto) o).getProgramName());
	}

}

