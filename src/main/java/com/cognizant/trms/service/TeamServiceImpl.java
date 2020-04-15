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
			Optional<Team> team = Optional
					.ofNullable(teamRepository.findByteamNameAndProgramId(teamName, program));

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
				return TeamMapper.toTeamDto(teamModel);
			}
			throw exceptionWithId(EntityType.TEAM, ExceptionType.DUPLICATE_ENTITY, teamName);
		}
		throw exceptionWithId(EntityType.PROGRAM, ExceptionType.BAD_REQUEST, programId);
	}
	
	
	/**
	 * Add Team members to existing team
	 */
	@Override
	public TeamDto addTeamMembersToTeam(TeamCreateRequest teamCreateRequest) throws JsonProcessingException {
		String teamId = teamCreateRequest.getId();
		// String teamName = teamCreateRequest.getTeamName().toLowerCase();
		String[] teamMemberIdsSelected = teamCreateRequest.getTeamMembers();

		Optional<Team> existingTeam = teamRepository.findById(teamId);

		if (existingTeam.isPresent()) {
			if (teamMemberIdsSelected != null) {
				Set<String> teamMembers = Arrays.stream(teamMemberIdsSelected).collect(Collectors.toSet());
				Set<User> teamMembersSet = getAllTeamMembers(teamMembers);
				if (!teamMembersSet.isEmpty()) {

					if (existingTeam.get().getTeamMembers() == null) {
						existingTeam.get().setTeamMembers(new HashSet<>());
					}
					existingTeam.get().getTeamMembers().addAll(teamMembersSet);
					return TeamMapper.toTeamDto(teamRepository.save(existingTeam.get()));
				}
				throw exceptionWithId(EntityType.USER, ExceptionType.BAD_REQUEST, teamId);
			}

		}
		throw exceptionWithId(EntityType.TEAM, ExceptionType.BAD_REQUEST, teamId);

	}
	

	@Transactional(readOnly = true)
	public Set<User> getAllTeamMembers(Set<String> teamMemberIds) {
		Iterable<User> iterable = userRepository.findAllById(teamMemberIds);
		Set<User> teamSet = new HashSet<User>((Collection<? extends User>) iterable);
		return teamSet;
	}

	@Override
	public TeamDto updateTeam(TeamCreateRequest TeamCreateRequest) throws JsonProcessingException {
		// TODO Auto-generated method stub
		return null;
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

		//user.setRoles(new HashSet<>(Arrays.asList(role)));
		userRepository.save(user);
	}

	private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id,
			String... args) {
		return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
	}

}
