/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Set;

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
	ProgramDto createProgram(ProgramDto programDto);

	/**
	 * Update program
	 *
	 * @param programDto
	 * @return
	 */
	ProgramDto updateProgram(ProgramDto programDto);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Boolean deleteProgram(String id);
}
