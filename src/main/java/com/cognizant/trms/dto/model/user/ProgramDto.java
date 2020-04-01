/**
 * 
 */
package com.cognizant.trms.dto.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author Vara Kotha
 *
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramDto implements Comparable {

	private String programId;
	private String programName;
	private String accountId;
	private String userId;
	private String accountName;
	private String programMgrEmail;
	private String programMgrFullName;
	private AccountDto account;
	private UserDto programMgr;

	@Override
	public int compareTo(Object o) {
		return this.getProgramName().compareTo(((ProgramDto) o).getProgramName());
	}

}