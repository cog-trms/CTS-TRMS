package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.opportunity.Candidate;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface CandidateRepository extends MongoRepository<Candidate, String> {
	
	Candidate findByCandidateEmail(String candidateEmail);
	
	List<Candidate> findByIsActive(boolean isActive);
}
