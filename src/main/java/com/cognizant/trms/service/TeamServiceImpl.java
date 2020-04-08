/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cognizant.trms.controller.v1.request.TeamCreateRequest;
import com.cognizant.trms.dto.mapper.TeamMapper;
import com.cognizant.trms.dto.model.user.TeamDto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;
import com.cognizant.trms.model.user.Role;
import com.cognizant.trms.model.user.Team;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.model.user.UserRole;
import com.cognizant.trms.model.user.UserRoles;
import com.cognizant.trms.repository.user.ProgramRepository;
import com.cognizant.trms.repository.user.RoleRepository;
import com.cognizant.trms.repository.user.TeamRepository;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.repository.user.UserRoleRepository;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author Vara Kotha
 *
 */

@Component
public class TeamServiceImpl implements TeamService {

	private static final Logger log = LogManager.getLogger(TeamServiceImpl.class);

	@Autowired
	private ProgramRepository programRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	/***
	 * Return all Team details
	 * 
	 * @return TeamDto
	 */

	@Override
	public Set<TeamDto> listTeams() {
		return teamRepository.findAll().stream().map(listTeams -> TeamMapper.toTeamDto(listTeams))
				.collect(Collectors.toCollection(TreeSet::new));
	}

	/**
	 * Create team
	 */
	@Override
	public TeamDto createTeam(TeamCreateRequest teamCreateRequest) throws JsonProcessingException {
		String programId = teamCreateRequest.getProgramId();
		String teamName = teamCreateRequest.getTeamName();
		String teamMembers = teamCreateRequest.getTeamMembers();

		Optional<Program> prog = programRepository.findById(programId);

		if (prog.isPresent()) {
			Program program = prog.get();
			Optional<Team> team = Optional.ofNullable(teamRepository.findByteamNameAndProgram(teamName, program));
			if (!team.isPresent()) {
				Optional<User> user = userRepository.findById(teamMembers);
				if (user.isPresent()) {
					User teamMember = user.get();
					Team teamModel = new Team().setTeamName(teamName.toLowerCase()).setProgram(program)
							.setTeamMembers(new HashSet<>(Arrays.asList(teamMember)));
					teamModel = teamRepository.save(teamModel);
					createUserRoles(program.getProgramMgr(), program.getAccount(), program, teamModel);
					return TeamMapper.toTeamDto(teamModel);
				}

				throw exceptionWithId(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, teamName,
						"Team members not found");
			}
			throw exceptionWithId(EntityType.TEAM, ExceptionType.DUPLICATE_ENTITY, teamName);
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.ENTITY_NOT_FOUND, programId);
	}

	@Override
	public TeamDto updateTeam(TeamCreateRequest TeamCreateRequest) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean deleteTeam(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TeamDto> getTeamsByProgramId(String Id) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TeamDto getTeamsById(String Id) {
		// TODO Auto-generated method stub
		return null;
	}

	/***
	 * Insert entry at userrole TODO: Move to common utility
	 * 
	 * @param user
	 * @param account
	 * @param program
	 */
	private void createUserRoles(User user, Account account, Program program, Team team)
			throws JsonProcessingException {
		Role role = roleRepository.findByRole(UserRoles.PROGRAM_MANAGER.name());
		UserRole userRole = new UserRole().setUser(user).setAccount(account).setProgram(program).setTeam(team)
				.setRole(role);
		userRoleRepository.save(userRole);

		user.setRoles(new HashSet<>(Arrays.asList(role)));
		userRepository.save(user);
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
