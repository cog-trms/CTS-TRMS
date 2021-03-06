/**
 * 
 */
package com.cognizant.trms.service;

import java.util.List;
import java.util.Set;

import com.cognizant.trms.controller.v1.request.ProgramCreationRequest;
import com.cognizant.trms.controller.v1.request.ProgramUpateRequest;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.fasterxml.jackson.core.JsonProcessingException;

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
	 * @throws JsonProcessingException
	 */
	ProgramDto updateProgram(ProgramUpateRequest programUpateRequest) throws JsonProcessingException;

	/**
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteProgram(String id);

	List<ProgramDto> getProgramsByAccountId(String Id) throws JsonProcessingException;

	ProgramDto getProgramById(String Id);
}
