/**
 * 
 */
package com.cognizant.trms.dto.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.dto.model.user.ProgramDto;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;
import com.cognizant.trms.model.user.User;

/**
 * @author Vara Kotha
 *
 */
public class ProgramMapper {

	private static final Logger log = LogManager.getLogger(ProgramMapper.class);

	public static ProgramDto toProgramDto(Program program) {

		Account account = program.getAccount();
		User programMgr = program.getProgramMgr();

		return new ProgramDto().setProgramName(program.getProgramName()).setId(program.getId())
				.setAccount(new AccountDto().setId(account.getId()).setAccountName(account.getAccountName()))
				.setProgramManager(new UserDto().setId(programMgr.getId()).setFirstName(programMgr.getFirstName())
						.setLastName(programMgr.getLastName()));
	}

}