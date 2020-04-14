package com.cognizant.trms.service;

import com.cognizant.trms.controller.v1.request.MapCandidateToCase;
import com.cognizant.trms.controller.v1.request.MapCandidateToInterview;
import com.cognizant.trms.controller.v1.request.MapCandidateToSo;
import com.cognizant.trms.controller.v1.request.SOCreateRequest;
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
import com.cognizant.trms.repository.opportunity.*;
import com.cognizant.trms.repository.user.CandidateRepository;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.util.TRMSUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.config.EnableMongoAuditing;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

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
	ModelMapper modelMapper;

	@Autowired
	CaseCandidateRepository caseCandidateRepository;

	@Autowired
	InterviewRepository interviewRepository;


	@Override
	public SODto createSO(SOCreateRequest soCreateRequest) throws JsonProcessingException {
		SO newSO = soRepository.findByServiceOrder(soCreateRequest.getServiceOrder());
		if (newSO == null) {
			newSO = new SO().setServiceOrder(soCreateRequest.getServiceOrder())
					.setLocation(soCreateRequest.getLocation())
					// VARA - TODO -- Get the user from token and update the CreatedBy Field //
					// .setCreatedBy(soCreateRequest.getCreatedBy())
					// VARA - TODO - END //
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
		String candidateId = mapCandidateToCase.getSoMappedCandidateId();
		String caseId = mapCandidateToCase.getSoCaseId();
		Optional<SOCase> existingCase = soCaseRepository.findById(caseId);
		if (existingCase.isPresent()) {
			SOCandidate soCandidate = soCandidateRepository.findByCandidateId(candidateId);
			if (soCandidate != null) {
				if (soCandidate.isActive()) {
					CaseCandidate caseCandidate = new CaseCandidate().setSoCaseId(caseId)
							.setSoMappedCandidateId(candidateId).setStatus(CASE_CANDIDATE_STATUS.MAPPED.getValue()); // Setting
																														// the
																														// initial
																														// value
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
			if (existingCaseCandidate.get().getSoMappedCandidateId().equals(candidateId)) {
				Interview interview = new Interview().setCaseCandidateId(caseCadidateId).setCandidateId(candidateId)
						.setInterviewStatus(CASE_CANDIDATE_INTERVIEW_STATUS.PANEL_ASSIGNED.getValue())
						.setPanelUserId(mapCandidateToInterview.getPanelUserId());
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
		PANEL_ASSIGNED("PANEL_ASSIGNED"), SCHEDULED("SCHEDULED"), INTERVIEW_SLOT_REQUESTED("INTERVIEW_SLOT_REQUESTED");

		String value;

		CASE_CANDIDATE_INTERVIEW_STATUS(String value) {
			this.value = value;
		}

		String getValue() {
			return this.value;
		}
	}

	@Override
	public List<SODto> getSOByLoginUser() throws JsonProcessingException {
		String username = TRMSUtil.loginUserName();
		log.debug("loginUserName" + username);

		// TODO: Verify whether logged in USER have HIRING_MANAGER/PROGRAM_MANAGER role

		List<SO> SOList = soRepository.findByCreateUser(username);

		if (!SOList.isEmpty()) {
			return SOList.stream().filter(so -> so != null).map(so -> SOMapper.toSODtoNoCase(so))
					.collect(Collectors.toList());
		}
		throw exceptionWithId(EntityType.SO, ExceptionType.ENTITY_NOT_FOUND, username);
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
		throw exceptionWithId(EntityType.SO, ExceptionType.BAD_REQUEST, soId);
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

}
