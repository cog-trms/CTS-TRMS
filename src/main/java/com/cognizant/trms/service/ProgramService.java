/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Set;

import com.cognizant.trms.controller.v1.request.ProgramCreationRequest;
import com.cognizant.trms.controller.v1.request.ProgramUpateRequest;
import com.cognizant.trms.dto.model.user.ProgramDto;

/**
 * @author Vara Kotha
 *
 */
public interface ProgramService {

	/**
	 * Return all program details
	 * 
	 * @return
	 */
	Set<ProgramDto> listPrograms();

	/**
	 * Create program
	 *
	 * @param programDto
	 * @return
	 */
	ProgramDto createProgram(ProgramCreationRequest programCreationReq);

	/**
	 * Update program
	 *
	 * @param programDto
	 * @return
	 */
	ProgramDto updateProgram(ProgramUpateRequest programUpateRequest);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteProgram(String id);
}
