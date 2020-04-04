package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.SOCase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SOCaseRepository extends MongoRepository<SOCase, String > {

}
