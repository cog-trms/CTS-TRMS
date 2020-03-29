/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.model.user.Program;

/**
 * @author Vara Kotha
 *
 */
public class ProgramMapper {
	
	private static final Logger log = LogManager.getLogger(ProgramMapper.class);
	
	public static ProgramDto toProgramDto(Program program) {
		
		log.debug("Program Name ->"+program.getProgramName());
		log.debug("Program Account Name -> "+program.getAccount().getAccountName());
		log.debug("Program Manager Name ->"+program.getProgramMgr().getFullName());
		
		return new ProgramDto()
				.setProgramName(program.getProgramName())
				.setAccountName(program.getAccount().getAccountName())
				.setProgramMgrEmail(program.getProgramMgr().getEmail())
				.setProgramMgrFullName(program.getProgramMgr().getFullName());
	}

}
