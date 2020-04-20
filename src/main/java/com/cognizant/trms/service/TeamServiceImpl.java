/**
 * 
 */
package com.cognizant.trms.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cognizant.trms.controller.v1.request.TeamCreateRequest;
import com.cognizant.trms.dto.mapper.ProgramMapper;
import com.cognizant.trms.dto.mapper.TeamMapper;
import com.cognizant.trms.dto.model.user.ProgramDto;
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
import com.cognizant.trms.util.TRMSUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vara Kotha
 *
 */

@Component
//@EnableMongoAuditing
public class TeamServiceImpl implements TeamService {

	private static final Logger log = LogManager.getLogger(TeamServiceImpl.class);

	@Autowired
	private ObjectMapper objectMapper;

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

	/**
	 * List all teams
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
		String teamName = teamCreateRequest.getTeamName().toLowerCase();
		String[] teamMemberIdsSelected = teamCreateRequest.getTeamMembers();

		Optional<Program> prog = programRepository.findById(programId);

		if (prog.isPresent()) {
			Program program = prog.get();
			Optional<Team> team = Optional.ofNullable(teamRepository.findByteamNameAndProgramId(teamName, program));

			if (!team.isPresent()) {
				Team teamModel = new Team().setTeamName(teamName.toLowerCase()).setProgram(program);
				if (teamMemberIdsSelected != null) {
					Set<String> teamMembers = Arrays.stream(teamMemberIdsSelected).collect(Collectors.toSet());
					Set<User> teamMembersSet = getAllTeamMembers(teamMembers);
					if (!teamMembersSet.isEmpty()) {
						teamModel.setTeamMembers(teamMembersSet);
					}
				}
				teamModel = teamRepository.save(teamModel);
				createUserRoles(program.getProgramMgr(), program.getAccount(), program,teamModel);
				return TeamMapper.toTeamDto(teamModel);
			}
			throw exceptionWithId(EntityType.TEAM, ExceptionType.DUPLICATE_ENTITY, teamName);
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.BAD_REQUEST, programId);
	}

	@Override
	public TeamDto updateTeam(TeamCreateRequest teamCreateRequest) throws JsonProcessingException {
		String teamId = teamCreateRequest.getId();
		String programId = teamCreateRequest.getProgramId();
		String teamName = teamCreateRequest.getTeamName().toLowerCase();
		String[] teamMemberIdsSelected = teamCreateRequest.getTeamMembers();

		Optional<Team> team = teamRepository.findById(teamId);

		if (team.isPresent()) {
			Team updatedTeam = team.get();
			Optional<Program> prog = programRepository.findById(programId);
			if (prog.isPresent()) {
				Program program = prog.get();
				if (!teamName.equals(team.get().getTeamName().toLowerCase())) {
					Optional<Team> teamWithName = Optional
							.ofNullable(teamRepository.findByteamNameAndProgramId(teamName, program));
					if (teamWithName.isPresent()) {
						throw exceptionWithId(EntityType.TEAM, ExceptionType.DUPLICATE_ENTITY, teamName,
								"Duplicate team name under the same program");
					}
				}
				if (teamMemberIdsSelected != null) {
					Set<String> teamMembers = Arrays.stream(teamMemberIdsSelected).collect(Collectors.toSet());
					Set<User> teamMembersSet = getAllTeamMembers(teamMembers);
					if (!teamMembersSet.isEmpty()) {

						updatedTeam.setTeamMembers(teamMembersSet);
					}
				}
				updatedTeam.setTeamName(teamName).setProgram(program);
				updatedTeam = teamRepository.save(updatedTeam);
				updateUserRoles(program.getProgramMgr(), program.getAccount(), program,updatedTeam);
				return TeamMapper.toTeamDto(updatedTeam);

			}
			throw exceptionWithId(EntityType.PROGRAM, ExceptionType.DUPLICATE_ENTITY, programId);

		}
		throw exceptionWithId(EntityType.TEAM, ExceptionType.BAD_REQUEST, teamId);

	}

	/**
	 * Add Team members to existing team
	 */
	@Override
	public TeamDto addTeamMembersToTeam(TeamCreateRequest teamCreateRequest) throws JsonProcessingException {
		String teamId = teamCreateRequest.getId();
		String programId = teamCreateRequest.getProgramId();
		String[] teamMemberIdsSelected = teamCreateRequest.getTeamMembers();

		Optional<Program> prog = programRepository.findById(programId);
		if (prog.isPresent()) {
			Team existingTeam = teamRepository.findByIdAndProgramId(teamId, prog.get());

			if (existingTeam != null) {
				if (teamMemberIdsSelected != null) {
					Set<String> teamMembers = Arrays.stream(teamMemberIdsSelected).collect(Collectors.toSet());
					Set<User> teamMembersSet = getAllTeamMembers(teamMembers);
					if (!teamMembersSet.isEmpty()) {

						if (existingTeam.getTeamMembers() == null) {
							existingTeam.setTeamMembers(new HashSet<>());
						}
						existingTeam.getTeamMembers().addAll(teamMembersSet);
						return TeamMapper.toTeamDto(teamRepository.save(existingTeam));
					}
					throw exceptionWithId(EntityType.USER, ExceptionType.BAD_REQUEST, teamId);
				}

			}
			throw exceptionWithId(EntityType.TEAM, ExceptionType.ENTITY_NOT_FOUND, teamId);

		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.BAD_REQUEST, programId);
	}
	
	@Transactional(readOnly = true)
	public Set<User> getAllTeamMembers(Set<String> teamMemberIds) {
		Iterable<User> iterable = userRepository.findAllById(teamMemberIds);
		Set<User> teamSet = new HashSet<User>((Collection<? extends User>) iterable);
		return teamSet;
	}



	/**
	 * @param teamId
	 * @return deleted or not
	 */
	@Override
	public Boolean deleteTeam(String id) {
		if (TRMSUtil.isAdmin()) {
			Optional<Team> team = teamRepository.findById(id);
			if (team.isPresent()) {
				teamRepository.deleteById(id);
				return true;
			}
			throw exceptionWithId(EntityType.TEAM, ExceptionType.BAD_REQUEST, id);
		}
		throw exceptionWithId(EntityType.TEAM, ExceptionType.ACCESS_DENIED,
				" Only an admin user can perform this operation");
	}

	/**
	 *
	 * @param programId
	 * @return List of Teams
	 */
	@Override
	public List<TeamDto> getTeamsByProgramId(String programId) throws JsonProcessingException {
		Optional<Program> prog = programRepository.findById(programId);
		if (prog.isPresent()) {
			Program program = prog.get();
			List<Team> teams = teamRepository.findByProgram(program);
			String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(teams);
			log.debug("GET PROGRAM BY NAME " + reqString);
			if (!teams.isEmpty()) {
				return teams.stream().filter(team -> team != null).map(team -> TeamMapper.toTeamDto(team))
						.collect(Collectors.toList());
			}
			throw exceptionWithId(EntityType.TEAM, ExceptionType.ENTITY_NOT_FOUND, program.getProgramName());
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.BAD_REQUEST, programId);
	}
	
	/**
	 *
	 * @param programId
	 * @return List of Teams
	 */
	@Override
	public ProgramDto getTeamsListByProgramId(String programId) throws JsonProcessingException {
		Optional<Program> prog = programRepository.findById(programId);
		if (prog.isPresent()) {
			Program program = prog.get();
			ProgramDto programDto = ProgramMapper.toProgramDto(program);
			List<Team> teams = teamRepository.findByProgram(program);
			List<TeamDto> teamListDto = null;
			String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(teams);
			log.debug("GET PROGRAM BY NAME " + reqString);
			if (!teams.isEmpty()) {
				 teamListDto=teams.stream().filter(team -> team != null).map(team -> TeamMapper.toMinTeamDto(team))
						.collect(Collectors.toList());
				 programDto.setTeamList(teamListDto);
				return programDto;
			}
			
			throw exceptionWithId(EntityType.TEAM, ExceptionType.ENTITY_NOT_FOUND, program.getProgramName());
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.BAD_REQUEST, programId);
	}


	@Override
	public TeamDto getTeamsById(String Id) {
		Optional<Team> team = teamRepository.findById(Id);
		if (team.isPresent()) {
			return TeamMapper.toTeamDto(team.get());
		}
		throw exceptionWithId(EntityType.TEAM, ExceptionType.BAD_REQUEST, Id);
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
	private void createUserRoles(User user, Account account, Program program,Team team) {
		Role role = roleRepository.findByRole(UserRoles.TEAM_MEMBER.name());
		UserRole userRole = new UserRole().setUser(user).setAccount(account).setProgram(program).setRole(role).setTeam(team);
		userRoleRepository.save(userRole);

		//user.setRoles(new HashSet<>(Arrays.asList(role)));
		//userRepository.save(user);
	}

	/**
	 * TODO: Move to common utility
	 * 
	 * @param user
	 * @param account
	 * @param program
	 * @throws JsonProcessingException
	 */
	private void updateUserRoles(User user, Account account, Program program,Team team) throws JsonProcessingException {
		Role role = roleRepository.findByRole(UserRoles.TEAM_MEMBER.name());
		UserRole existingUserRole = userRoleRepository.findByRoleIdAndAccountAndProgramAndTeam(role.getId(), account, program,team);

		if (existingUserRole == null) {
			createUserRoles(user, account, program,team);
		} else {
			existingUserRole.setUser(user);
			userRoleRepository.save(existingUserRole);
			//user.setRoles(new HashSet<>(Arrays.asList(role)));
			//userRepository.save(user);
		}

	}

}
