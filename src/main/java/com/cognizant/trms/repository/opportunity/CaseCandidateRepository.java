package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.CaseCandidate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CaseCandidateRepository extends MongoRepository<CaseCandidate,String> {
}
