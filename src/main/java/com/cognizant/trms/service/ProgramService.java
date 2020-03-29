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
	     * Register a new Program
	     *
	     * @param programDto
	     * @return
	     */
	 ProgramDto createProgram(ProgramDto programDto);
 

}
