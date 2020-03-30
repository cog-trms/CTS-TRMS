/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;

import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.model.user.Program;

/**
 * @author Vara Kotha
 *
 */
public class ProgramMapper {
	
	private static final Logger log = LogManager.getLogger(ProgramMapper.class);
	
	public static ProgramDto toProgramDto(Program program,String all) {

		return new ProgramDto()
				.setProgramName(program.getProgramName())
				.setAccount(new ModelMapper().map(program.getAccount(),AccountDto.class))
				.setProgramMgr(new ModelMapper().map(program.getProgramMgr(),UserDto.class));
	}
	
public static ProgramDto toProgramDto(Program program) {
		
		return new ProgramDto()
				.setProgramName(program.getProgramName())
				.setProgramId(program.getId())
				.setAccountId(program.getAccount().getId())
				.setAccountName(program.getAccount().getAccountName())
				.setUserId(program.getProgramMgr().getId())
				.setProgramMgrFullName(program.getProgramMgr().getFullName());
	}

}
