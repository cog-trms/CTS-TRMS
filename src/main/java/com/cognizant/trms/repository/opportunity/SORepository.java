package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.SO;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface SORepository extends MongoRepository<SO, String> {

    SO findByServiceOrder(String so);
    Set<SO> findByCreatedBy(String createdBy);
    Set<SO> findByTeamId(String teamId);
}
