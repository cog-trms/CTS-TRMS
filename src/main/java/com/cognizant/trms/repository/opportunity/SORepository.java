package com.cognizant.trms.repository.opportunity;

import com.cognizant.trms.model.opportunity.SO;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface SORepository extends MongoRepository<SO, String> {

    SO findByServiceOrder(String so);
    SO findByCreatedBy(String createdBy);

}
