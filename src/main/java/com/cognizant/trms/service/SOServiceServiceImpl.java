package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.*;
import com.cognizant.trms.dto.mapper.CaseCandidateMapper;
import com.cognizant.trms.dto.mapper.SOCaseMapper;
import com.cognizant.trms.dto.mapper.SOMapper;
import com.cognizant.trms.dto.model.opportunity.CaseCandidateDto;
import com.cognizant.trms.dto.model.opportunity.InterviewDto;
import com.cognizant.trms.dto.model.opportunity.SOCaseDto;
import com.cognizant.trms.dto.model.opportunity.SODto;
import com.cognizant.trms.exception.EntityType;
import com.cognizant.trms.exception.ExceptionType;
import com.cognizant.trms.exception.TRMSException;
import com.cognizant.trms.model.opportunity.*;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.model.user.UserRole;
import com.cognizant.trms.repository.opportunity.*;
import com.cognizant.trms.repository.user.CandidateRepository;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.repository.user.UserRoleRepository;
import com.cognizant.trms.util.TRMSUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
    Author: Aravindan Dandapani
*/
@Component
@EnableMongoAuditing
public class SOServiceServiceImpl implements SOService {

	private static final Logger log = LogManager.getLogger(SOServiceServiceImpl.class);

	@Autowired
	SORepository soRepository;

	@Autowired
	SOCaseRepository soCaseRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SOCandidateRepository soCandidateRepository;

	@Autowired
	CandidateRepository candidateRepository;
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CaseCandidateRepository caseCandidateRepository;

	@Autowired
	InterviewRepository interviewRepository;
	
	@Autowired
	UserService userService;
	
	
	@Autowired
	UserRoleRepository userRoleRepository;


	@Override
	public SODto createSO(SOCreateRequest soCreateRequest) throws JsonProcessingException {
		SO newSO = soRepository.findByServiceOrder(soCreateRequest.getServiceOrder());
		if (newSO == null) {
			newSO = new SO().setServiceOrder(soCreateRequest.getServiceOrder())
					.setLocation(soCreateRequest.getLocation())
					.setPositionCount(soCreateRequest.getPositionCount()).setTeamId(soCreateRequest.getTeamId());
			String reqString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newSO);
			log.debug("SO Request  " + reqString);

			SO createdSO = soRepository.save(newSO);
			log.debug("SO Response" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createdSO));
			String soId = createdSO.getId();
			List<SOCase> cases = null;
			if (!soCreateRequest.getCases().isEmpty()) {
				cases = soCreateRequest.getCases().stream().map(item -> SOMapper.toCaseReqToCase(item, soId))
						.collect(Collectors.toList());
				log.debug("Case Request" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cases));

				cases = soCaseRepository.saveAll(cases);
				log.debug("Case Response" + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cases));

				createdSO.setCases(cases);
				createdSO = soRepository.save(createdSO);

			}
			return SOMapper.toSODto(createdSO, cases);
		}
		throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.DUPLICATE_ENTITY,
				soCreateRequest.getServiceOrder());
	}

	@Override
	public SODto addCandidateToSo(MapCandidateToSo mapCandidateToSo) {
		String candidateId = mapCandidateToSo.getCandidateId();
		String soId = mapCandidateToSo.getSoId();
		Optional<SO> existingSo = soRepository.findById(soId);
		if (existingSo.isPresent()) {
			Optional<Candidate> candidate = candidateRepository.findById(candidateId);
			if (candidate.isPresent()) {
				if (candidate.get().isActive()) {
					SOCandidate soCandidate = new SOCandidate().setCandId(mapCandidateToSo.getCandidateId())
							.setCandidate(candidate.get()).setActive(mapCandidateToSo.isActive())
							.setSoId(mapCandidateToSo.getSoId());
					soCandidateRepository.save(soCandidate);

					// Updating the SO_CANDIDATE ref in SO collection
					if (existingSo.get().getCandidates() == null) {
						existingSo.get().setCandidates(new ArrayList<>());
					}
					existingSo.get().getCandidates().add(soCandidate);
					return SOMapper.toSODtoNoCase(soRepository.save(existingSo.get()));
					// return modelMapper.map( soRepository.save(existingSo.get()), SODto.class);
				}
				throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, candidateId + " not active");
			}
			throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.BAD_REQUEST, candidateId + " is not found");
		}
		throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.BAD_REQUEST, soId + " not found");
	}

	@Override
	public CaseCandidateDto addCandidateToCase(MapCandidateToCase mapCandidateToCase) {
		String candidateId = mapCandidateToCase.getCandidateId();
		String caseId = mapCandidateToCase.getSoCaseId();

		Optional<SOCase> existingCase = soCaseRepository.findById(caseId);
		if (existingCase.isPresent()) {
			SOCandidate soCandidate = soCandidateRepository.findByCandidateId(candidateId);
			if (soCandidate != null) {

				if (soCandidate.isActive()) {

					CaseCandidate existingCaseCandidate = caseCandidateRepository.findBySoCaseIdAndCandidateId(caseId, candidateId);
					if (existingCaseCandidate == null) {


						CaseCandidate caseCandidate = new CaseCandidate().setSoCaseId(caseId)
								.setCandidateId(candidateId)
								.setStatus(CASE_CANDIDATE_STATUS.MAPPED.getValue()); // Setting the initial value

						caseCandidate = caseCandidateRepository.save(caseCandidate);

						// START - Updating the CASE_CANDIDATE ref in CASE collection
						if (existingCase.get().getCaseCandidates() == null) {
							existingCase.get().setCaseCandidates(new ArrayList<>());
						}
						existingCase.get().getCaseCandidates().add(caseCandidate);
						soCaseRepository.save(existingCase.get());
						// END - Updating the CASE_CANDIDATE ref in CASE collection

						return modelMapper.map(caseCandidate, CaseCandidateDto.class);
					}
					throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.DUPLICATE_ENTITY,
							"Candidate id " + candidateId + " is already exists in case  " + caseId);

				}
				throw exceptionWithId(EntityType.SO_CANDIDATE, ExceptionType.BAD_REQUEST,
						"Candidate id " + candidateId + " is not active in SO " + soCandidate.getSoId());
			}
			throw exceptionWithId(EntityType.SO_CANDIDATE, ExceptionType.BAD_REQUEST,
					"Candidate id " + candidateId + " is not found in SO");
		}
		throw exceptionWithId(EntityType.CASE, ExceptionType.BAD_REQUEST, caseId + " is not found");
	}

	@Override
	public InterviewDto addCandidateToInterview(MapCandidateToInterview mapCandidateToInterview) {
		String caseCadidateId = mapCandidateToInterview.getCaseCandidateId();
		String candidateId = mapCandidateToInterview.getCandidateId();
		// ***** VARA - TODO-START *****//
		// CHECK WHETHER THE PANEL USER ID IS A TEAM MEMBER OF THE ACCOUNT/TEAM ON WHICH
		// THIS SO HAS CREATED
		String panelUserId = mapCandidateToInterview.getPanelUserId();
		// ***** VARA - TODO-END *****//
		Optional<CaseCandidate> existingCaseCandidate = caseCandidateRepository.findById(caseCadidateId);
		if (existingCaseCandidate.isPresent()) {
			if (existingCaseCandidate.get().getCandidateId().equals(candidateId)) {
				//log.debug("InterviewDate"+mapCandidateToInterview.getInterviewDate());
				Interview interview = new Interview().setCaseCandidateId(caseCadidateId).setCandidateId(candidateId)
						.setInterviewStatus(CASE_CANDIDATE_INTERVIEW_STATUS.PANEL_ASSIGNED.getValue())
						.setPanelUserId(mapCandidateToInterview.getPanelUserId())
						.setInterviewDate(mapCandidateToInterview.getInterviewDate());
				interview = interviewRepository.save(interview);

				// START - Updating the INTERVIEW ref in CASE_CANDIDATE collection
				if (existingCaseCandidate.get().getInterviews() == null) {
					existingCaseCandidate.get().setInterviews(new ArrayList<>());
				}
				existingCaseCandidate.get().getInterviews().add(interview);
				caseCandidateRepository.save(existingCaseCandidate.get());
				// END - Updating the INTERVIEW ref in CASE_CANDIDATE collection

				return modelMapper.map(interview, InterviewDto.class);

			}
			throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.BAD_REQUEST,
					"Candidate id " + candidateId + " is not found in CaseCandidate " + caseCadidateId);
		}
		throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.BAD_REQUEST,
				"CaseCandidate id " + caseCadidateId + " is not found");
	}

	@Override
	public CaseCandidateDto updateCandidateStatus(CaseCandidateStatusUpdateRequest caseCandidateStatusUpdateRequest) {
		String caseCandidateId = caseCandidateStatusUpdateRequest.getCaseCandidateId();
		String caseCandidateStaus = caseCandidateStatusUpdateRequest.getStatus();
		Optional<CaseCandidate> existingCC = caseCandidateRepository.findById(caseCandidateId);
		if (existingCC.isPresent()) {
			if (
					Stream.of(CASE_CANDIDATE_INTERVIEW_STATUS.values())
							.anyMatch(v -> v.getValue().equals(caseCandidateStaus))
			) {
				Optional<SOCase> existingCase = soCaseRepository.findById(existingCC.get().getSoCaseId());
				// Increment the No.of.Selected in CASE entity.
				if (caseCandidateStaus.equals(CASE_CANDIDATE_INTERVIEW_STATUS.SELECTED)) {
					if (existingCase.isPresent()) {

						existingCase.get().setNumberOfPosition(existingCase.get().getNumberOfSelected() + 1);
						soCaseRepository.save(existingCase.get());
					}
				}
				// Decrement the No.of.Selected in CASE entity.
				else if (existingCC.get().getStatus().equals(CASE_CANDIDATE_INTERVIEW_STATUS.SELECTED) &&
						(!caseCandidateStaus.equals(CASE_CANDIDATE_INTERVIEW_STATUS.SELECTED))) {
					if (existingCase.isPresent()) {
						existingCase.get().setNumberOfPosition(existingCase.get().getNumberOfSelected() - 1);
						soCaseRepository.save(existingCase.get());
					}

				}
				existingCC.get().setStatus(caseCandidateStatusUpdateRequest.getStatus());
				return modelMapper.map(caseCandidateRepository.save(existingCC.get()), CaseCandidateDto.class);
			}
			throw exceptionWithId(EntityType.CASE_CANDIDATE_STATUS, ExceptionType.BAD_REQUEST,
					"Request body contains the invalid status value " + caseCandidateStaus);
		}
		throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.BAD_REQUEST,
				"CaseCandidate id " + caseCandidateId + " is not found");
	}

	@Override
	public boolean deleteCaseCandidate(String caseCandidateId) {
		Optional<CaseCandidate> existingCC = caseCandidateRepository.findById(caseCandidateId);
		String caseId = existingCC.get().getSoCaseId();
		if(existingCC.isPresent()){

			caseCandidateRepository.deleteById(caseCandidateId);
			Optional<SOCase> existingCase = soCaseRepository.findById(caseId);
			if(existingCase.isPresent()){
				existingCase.get().getCaseCandidates().remove(existingCC.get());
				soCaseRepository.save(existingCase.get());
			}
			return true;
		}
		throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.BAD_REQUEST,
				"CaseCandidate id " + caseCandidateId + " is not found");
	}

	//TODO - This operation can't be UNDO. So appropriate validation has to happen at UI and in Service
	// TODO - Only on HIRING_MANAGER of this SO can perform this activity
	// TODO - (For now just checked whether logged in user is HM, but didn't check whether he is HM for this SO)
	@Override
	public CaseCandidateDto onboardCaseCandidate(String caseCandidateId) {
		if (TRMSUtil.isHiringManager()) {
			Optional<CaseCandidate> existingCC = caseCandidateRepository.findById(caseCandidateId);
			if (existingCC.isPresent()) {
				Optional<SOCase> existingCase = soCaseRepository.findById(existingCC.get().getSoCaseId());
				if (existingCase.isPresent()) {
					existingCase.get().setNumberOfFilled(existingCase.get().getNumberOfFilled() + 1);
					soCaseRepository.save(existingCase.get());
				}
				existingCC.get().setOnBoarded(true);
				return modelMapper.map(caseCandidateRepository.save(existingCC.get()), CaseCandidateDto.class);
			}
			throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.BAD_REQUEST,
					"CaseCandidate id " + caseCandidateId + " is not found");

		}
		throw exceptionWithId(EntityType.CASE_CANDIDATE, ExceptionType.ACCESS_DENIED,
				"Only Hiring Manager can perform this activity");

	}


	@Override
	public Set<SODto> getSOByLoginUser() throws JsonProcessingException {

		// Get logged in userName
		String username = TRMSUtil.loginUserName();

		// Get logged in user object
		Optional<User> user = Optional.ofNullable(userRepository.findByEmail(username.toLowerCase()));

		// Get logged in user roles
		List<String> userRoleList = userService.getUserRoleByUser(user.get());
		if (userRoleList != null) {
			userRoleList.stream().distinct().collect(Collectors.toList());
		}

		// Find logged in user eligible teamIds
		Set<String> teamSet = new HashSet<>();
		Set<SO> SOList = new HashSet<>();

		if (userRoleList.contains("HIRING_MANAGER")) {
			log.debug("HIRING_MANAGER");
			teamSet = SOTeamIdsByAccount(user.get());
			SOList = getAllSO(teamSet);
			log.debug("SOLISt size" + SOList.size());
		} else if (userRoleList.contains("PROGRAM_MANAGER")) {
			log.debug("PROGRAM_MANAGER");
			teamSet = SOTeamIdsByProgram(user.get());
			SOList = getAllSO(teamSet);
			log.debug("SOLISt size" + SOList.size());
		}

		if (!SOList.isEmpty()) {
			return SOList.stream().filter(so -> so != null).map(so -> SOMapper.toSODtoNoCase(so))
					.collect(Collectors.toSet());

		}
		throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.ENTITY_NOT_FOUND, username);
	}
	
	/**
	 * Find the list of teams based on program
	 * @param user
	 * @return
	 */
	public Set<String> SOTeamIdsByProgram(User user) {
		List<UserRole> userRoleProgSet = userRoleRepository.findProgramByUser(user);
		log.debug(userRoleProgSet.size());
		Set<String> soTeamSet = new HashSet<>();
		userRoleProgSet.forEach(userRole -> {
			log.debug("ProgramId:" + userRole.getProgram().getId());
			List<UserRole> userRoleTeamSet = userRoleRepository.findByProgram(userRole.getProgram());
			userRoleTeamSet.forEach(userTeamRole -> {
				if (userTeamRole.getTeam() != null) {
					log.debug(("teamId:" + userTeamRole.getTeam().getId()));
					soTeamSet.add(userTeamRole.getTeam().getId());
				}
			});
			log.debug("SO team List" + soTeamSet.toString());
		});
		return soTeamSet;
	}
	
	/**
	 * Find the list of teams based on account
	 * @param user
	 * @return
	 */
	public Set<String> SOTeamIdsByAccount(User user) {
		List<UserRole> userRoleAccSet = userRoleRepository.findAccountByUser(user);
		log.debug(userRoleAccSet.size());
		Set<String> soTeamSet = new HashSet<>();
		userRoleAccSet.forEach(userRole -> {
			log.debug("AccountId:" + userRole.getAccount().getId());
			List<UserRole> userRoleTeamSet = userRoleRepository.findByAccount(userRole.getAccount());
			userRoleTeamSet.forEach(userTeamRole -> {
				if (userTeamRole.getTeam() != null) {
					log.debug(("teamId:" + userTeamRole.getTeam().getId()));
					soTeamSet.add(userTeamRole.getTeam().getId());
				}
			});
			log.debug("SO team List" + soTeamSet.toString());
		});
		return soTeamSet;
	}
	
	@Transactional(readOnly = true)
	public Set<SO> getAllSO(Set<String> teamIds) {
		Set<SO> SOSet = new HashSet<SO>();
		teamIds.forEach(teamId -> {
			SOSet.addAll(soRepository.findByTeamId(teamId));
		});
		return SOSet;
	}

	@Override
	public SODto getCasesBySO(String soId) throws JsonProcessingException {
		// SO serviceOrder = soRepository.findByServiceOrder(soId);
		Optional<SO> serviceOrder = soRepository.findById(soId);
		if (serviceOrder.isPresent()) {
			SO so = serviceOrder.get();
			SODto soDto = SOMapper.toSODtoNoCase(so);
			List<SOCase> soCases = soCaseRepository.findBysoId(soId);
			List<SOCaseDto> soCaseListDto = null;
			if (!soCases.isEmpty()) {
				soCaseListDto = soCases.stream().filter(soCase -> soCase != null)
						.map(soCase -> SOCaseMapper.toSOCaseDto(soCase)).collect(Collectors.toList());
				soDto.setCases(soCaseListDto);
				return soDto;
			}
			throw exceptionWithId(EntityType.CASE, ExceptionType.ENTITY_NOT_FOUND, soId);

		}
		throw exceptionWithId(EntityType.SERVICEORDER, ExceptionType.BAD_REQUEST, soId);
	}
	
	@Override
	public List<InterviewDto> getInterviewsByPanelUserId(String panelUserId) throws JsonProcessingException {

		List<Interview> interviewList = interviewRepository.findByPanelUserId(panelUserId);

			if (!interviewList.isEmpty()) {
				return interviewList.stream().filter(interview -> interview != null)
				.map(interview -> new ModelMapper().map(interview,InterviewDto.class))
				.collect(Collectors.toList());
			}
			throw exceptionWithId(EntityType.INTERVIEW, ExceptionType.ENTITY_NOT_FOUND, panelUserId);
	}
	

	@Override
	public List<CaseCandidateDto> getCaseCandidateByCaseId(String soCaseId) throws JsonProcessingException {
		List<CaseCandidate> caseCandidates = caseCandidateRepository.findBysoCaseId(soCaseId);

		if (!caseCandidates.isEmpty()) {
			return caseCandidates.stream().filter(caseCandidate -> caseCandidate != null)
					.map(caseCandidate -> CaseCandidateMapper.toCaseCandidateDto(caseCandidate))
					.collect(Collectors.toList());
		}
		throw exceptionWithId(EntityType.CANDIDATE, ExceptionType.ENTITY_NOT_FOUND, soCaseId);
	}


	private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
		return TRMSException.throwException(entityType, exceptionType, args);
	}

	private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id,
											 String... args) {
		return TRMSException.throwExceptionWithId(entityType, exceptionType, id, args);
	}

	public enum CASE_CANDIDATE_STATUS {
		MAPPED("MAPPED"), SCHEDULED("SCHEDULED"), INTERVIEW_SLOT_REQUESTED("INTERVIEW_SLOT_REQUESTED");

		String value;

		CASE_CANDIDATE_STATUS(String value) {
			this.value = value;
		}

		String getValue() {
			return this.value;
		}
	}

	public enum CASE_CANDIDATE_INTERVIEW_STATUS {
		PANEL_ASSIGNED("PANEL_ASSIGNED"),
		SCHEDULED("SCHEDULED"),
		INTERVIEW_SLOT_REQUESTED("INTERVIEW_SLOT_REQUESTED"),
		INTERVIEW_IN_PROGRESS("INTERVIEW_IN_PROGRESS"),
		SELECTED("SELECTED"),
		IN_ACTIVE("IN_ACTIVE");


		String value;

		CASE_CANDIDATE_INTERVIEW_STATUS(String value) {
			this.value = value;
		}

		String getValue() {
			return this.value;
		}
	}

}
