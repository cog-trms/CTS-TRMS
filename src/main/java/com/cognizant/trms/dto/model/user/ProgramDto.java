
package com.cognizant.trms.dto.model.user;

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

public class ProgramDto implements Comparable{
    private String id;

    private String programName;
    private AccountDto account;
    //private String programMgrId;
    private UserDto programManager;

	@Override
	public int compareTo(Object o) {
		return this.getProgramName().compareTo(((ProgramDto) o).getProgramName());
	}

}

