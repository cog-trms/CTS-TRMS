/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Set;

import com.cognizant.trms.dto.model.user.ProgramDto;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognizant.trms.controller.v1.request.ProgramCreationRequest;
import com.cognizant.trms.controller.v1.request.ProgramUpateRequest;
import com.cognizant.trms.dto.mapper.ProgramMapper;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.repository.user.AccountRepository;
import com.cognizant.trms.repository.user.ProgramRepository;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.util.AuthUtil;

/**
 * @author Vara Kotha
 *
 */

@Component
public class ProgramServiceImpl implements ProgramService {

	private static final Logger log = LogManager.getLogger(ProgramServiceImpl.class);

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	/***
	 * Return all program details
	 * 
	 * @return programDto
	 */
	@Override
	public Set<ProgramDto> listPrograms() {
		return programRepository.findAll().stream().map(listPrograms -> ProgramMapper.toProgramDto(listPrograms))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	/***
	 * Create the program
	 * 
	 * @param programDto
	 * @return programDto
	 */
	@Override
	public ProgramDto createProgram(ProgramCreationRequest programCreationReq) {
		Account selAccount = null;
		User selProgramMgr = null;

		// Get the account object
		Optional<Account> account = accountRepository.findById(programCreationReq.getAccountId());

		if (account.isPresent()) {
			selAccount = account.get();
		} else {
			throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, programCreationReq.getAccountId());
		}
		
		// Get the user object
		Optional<User> programMgr = userRepository.findById(programCreationReq.getUserId());

		if (programMgr.isPresent()) {
			selProgramMgr = programMgr.get();
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, programCreationReq.getUserId());
		}
			

		// Verify whether same program already exist
		Program program = programRepository.findByprogramName(programCreationReq.getName());

		// Save the program
		if (program == null) {
			Program programModel = new Program().setProgramName(programCreationReq.getName()).setAccount(selAccount)
					.setProgramMgr(selProgramMgr);
			return ProgramMapper.toProgramDto(programRepository.save(programModel));
		}
		throw exception(EntityType.PROGRAM, ExceptionType.DUPLICATE_ENTITY, programCreationReq.getName());

	}

	/***
	 * Update the program
	 * 
	 * @param programDto
	 * @return programDto
	 */

	@Override
	public ProgramDto updateProgram(ProgramUpateRequest programUpdateRequest) {
		Account selAccount = null;
		User selProgramMgr = null;

		// Get the account object
		Optional<Account> account = accountRepository.findById(programUpdateRequest.getAccountId());

		if (account.isPresent()) {
			selAccount = account.get();
		} else {
			throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, programUpdateRequest.getAccountId());
		}

		// Get the user object
		Optional<User> programMgr = userRepository.findById(programUpdateRequest.getUserId());

		if (programMgr.isPresent()) {
			selProgramMgr = programMgr.get();
		} else {
			throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, programUpdateRequest.getUserId());
		}

		// Patch/update the program
		Optional<Program> program = programRepository.findById(programUpdateRequest.getProgramId());

		if (program.isPresent()) {
			Program programModel = program.get();
			programModel.setProgramName(programUpdateRequest.getName()).setAccount(selAccount)
					.setProgramMgr(selProgramMgr);

			return ProgramMapper.toProgramDto(programRepository.save(programModel));
		}
		throw exception(EntityType.PROGRAM, ExceptionType.ENTITY_NOT_FOUND, programUpdateRequest.getName());

	}

	/***
	 * Delete the Program
	 * 
	 * @param programDto
	 * @return programDto
	 */
	@Override
	public Boolean deleteProgram(String id) {
		if (AuthUtil.isAdmin()) {
			Optional<Program> program = programRepository.findById(id);
			if (program.isPresent()) {
				programRepository.deleteById(id);
				return true;
			}
			throw exceptionWithId(EntityType.PROGRAM, ExceptionType.ENTITY_NOT_FOUND, id);
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.ACCESS_DENIED,
				" Only an admin user can perform this operation");
	}

	/**
	 * Returns a new RuntimeException TODO: Need to move common utility logic
	 * 
	 * @param entityType
	 * @param exceptionType
	 * @param args
	 * @return
	 */
	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return TRMSException.throwException(entityType, exceptionType, args);
	}

	private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id,
			String... args) {
		return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
	}

}
