/**
 * 
 */
package com.cognizant.trms.service;
import java.util.Set;

import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.response.Response;

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
	     * Create or update program details
	     *
	     * @param programDto
	     * @return
	     */
	 ProgramDto saveOrUpdateProgram(ProgramDto programDto);
	 
	 
	 Response deleteProgram(String id);

}
