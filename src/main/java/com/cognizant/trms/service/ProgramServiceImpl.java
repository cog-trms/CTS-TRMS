/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Set;

import com.cognizant.trms.dto.model.user.ProgramDto;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import com.cognizant.trms.model.user.Role;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.model.user.UserRole;
import com.cognizant.trms.model.user.UserRoles;
import com.cognizant.trms.repository.user.AccountRepository;
import com.cognizant.trms.repository.user.ProgramRepository;
import com.cognizant.trms.repository.user.RoleRepository;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.repository.user.UserRoleRepository;
import com.cognizant.trms.util.AuthUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

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

		String accountId = programCreationReq.getAccountId();
		String programName = programCreationReq.getName().toLowerCase();
		String userId = programCreationReq.getUserId();

		Optional<Account> acc = accountRepository.findById(accountId);

		if (acc.isPresent()) {
			Account account = acc.get();
			Optional<Program> program = Optional
					.ofNullable(programRepository.findByProgramNameAndAccountId(programName, account));
			if (!program.isPresent()) {
				Optional<User> user = userRepository.findById(userId);
				if (user.isPresent()) {
					User programMgr = user.get();
					Program programModel = new Program().setProgramName(programName).setAccount(account)
							.setProgramMgr(programMgr);
					programModel = programRepository.save(programModel);
					createUserRoles(programMgr, account, programModel);
					return ProgramMapper.toProgramDto(programModel);
				}
				throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND);
			}
			throw exceptionWithId(EntityType.PROGRAM, ExceptionType.DUPLICATE_ENTITY, programName);
		}
		throw exception(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND);

	}

	/***
	 * Update the program
	 * 
	 * @param programDto
	 * @return programDto
	 * @throws JsonProcessingException
	 */

	@Override
	public ProgramDto updateProgram(ProgramUpateRequest programUpdateReq) throws JsonProcessingException {
		String accountId = programUpdateReq.getAccountId();
		String programName = programUpdateReq.getName().toLowerCase();
		String userId = programUpdateReq.getUserId();
		String programId = programUpdateReq.getProgramId();

		Optional<Program> existingProgram = programRepository.findById(programId);

		if (existingProgram.isPresent()) {
			Optional<Account> acc = accountRepository.findById(accountId);
			if (acc.isPresent()) {
				Account account = acc.get();
				Optional<User> user = userRepository.findById(userId);
				if (user.isPresent()) {
					User programMgr = user.get();
					if (!programName.equalsIgnoreCase(existingProgram.get().getProgramName())) {
						Optional<Program> programWithNameModel = Optional
								.ofNullable(programRepository.findByProgramNameAndAccountId(programName, account));
						if (programWithNameModel.isPresent()) {
							throw exceptionWithId(EntityType.PROGRAM, ExceptionType.DUPLICATE_ENTITY, programName,
									"Duplicate program name under the same Account");
						}
					}
					existingProgram.get().setProgramName(programName.toLowerCase()).setAccount(account)
							.setProgramMgr(programMgr);

					Program updatedProgram = programRepository.save(existingProgram.get());
					updateUserRoles(programMgr, account, updatedProgram);
					return ProgramMapper.toProgramDto(updatedProgram);

				}
				throw exception(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND);
			}
			throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, programName);
		}
		throw exception(EntityType.PROGRAM, ExceptionType.ENTITY_NOT_FOUND);
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

	/***
	 * Insert entry at userrole TODO: Move to common utility
	 * 
	 * @param user
	 * @param account
	 * @param program
	 */
	private void createUserRoles(User user, Account account, Program program) {
		Role role = roleRepository.findByRole(UserRoles.PROGRAM_MANAGER.name());
		UserRole userRole = new UserRole().setUser(user).setAccount(account).setProgram(program).setRole(role);
		userRoleRepository.save(userRole);

		user.setRoles(new HashSet<>(Arrays.asList(role)));
		userRepository.save(user);
	}

	/**
	 * TODO: Move to common utility
	 * 
	 * @param user
	 * @param account
	 * @param program
	 * @throws JsonProcessingException
	 */
	private void updateUserRoles(User user, Account account, Program program) throws JsonProcessingException {
		Role role = roleRepository.findByRole(UserRoles.PROGRAM_MANAGER.name());
		UserRole existingUserRole = userRoleRepository.findByRoleIdAndAccountAndProgram(role.getId(), account, program);

		if (existingUserRole == null) {
			createUserRoles(user, account, program);
		} else {
			existingUserRole.setUser(user);
			userRoleRepository.save(existingUserRole);
			user.setRoles(new HashSet<>(Arrays.asList(role)));
			userRepository.save(user);
		}

	}

	/**
	 * Get list of Programs by AccountID
	 */
	@Override
	public List<ProgramDto> getProgramsByAccountId(String accountId) throws JsonProcessingException {
		Optional<Account> acc = accountRepository.findById(accountId);
		if (acc.isPresent()) {
			Account account = acc.get();
			List<Program> programs = programRepository.findByAccount(account);
			String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(programs);
			log.debug("GET PROGRAM BY NAME " + reqString);
			if (!programs.isEmpty()) {
				return programs.stream().filter(program -> program != null)
						.map(program -> ProgramMapper.toProgramDto(program)).collect(Collectors.toList());
			}
			throw exceptionWithId(EntityType.PROGRAM, ExceptionType.ENTITY_NOT_FOUND, account.getAccountName());
		}
		throw exceptionWithId(EntityType.ACCOUNT, ExceptionType.ENTITY_NOT_FOUND, accountId);

	}

	/**
	 * Get program by programID
	 */
	@Override
	public ProgramDto getProgramById(String Id) {
		Optional<Program> program = programRepository.findById(Id);
		if (program.isPresent()) {
			return ProgramMapper.toProgramDto(program.get());
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.ENTITY_NOT_FOUND, Id);
	}

}
