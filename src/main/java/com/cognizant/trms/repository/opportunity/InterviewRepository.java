package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterviewRepository extends MongoRepository<Interview, String> {

    List<Interview> findByCandidateId(String candidateId);
    List<Interview> findByPanelUserId(String panelUserId);

}
