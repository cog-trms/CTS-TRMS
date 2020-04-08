package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.SOCandidate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SOCandidateRepository extends MongoRepository <SOCandidate, String> {
    SOCandidate findByCandidateId(String candidateId);
}
