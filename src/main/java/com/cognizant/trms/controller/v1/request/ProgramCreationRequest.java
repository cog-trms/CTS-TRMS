/**
 * 
 */
package com.cognizant.trms.controller.v1.request;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Vara Kotha
 *
 */


@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramCreationRequest {
	
	@NotEmpty(message = "{constraints.NotEmpty.message}")
    private String programName;
	
	// To retrieve Account object
	private String accountName;
	
	// To retrieve Program Manager user object
	private String programMgrEmail;

}
