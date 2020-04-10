package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.SOCase;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SOCaseRepository extends MongoRepository<SOCase, String> {

	List<SOCase> findBysoId(String soId);

}
