package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.CaseCandidate;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseCandidateRepository extends MongoRepository<CaseCandidate,String> {
	
	List<CaseCandidate> findBysoCaseId(String soCaseId);
}
