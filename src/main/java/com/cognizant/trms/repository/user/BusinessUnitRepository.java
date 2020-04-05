package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.BusinessUnit;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusinessUnitRepository extends MongoRepository<BusinessUnit, String> {
    BusinessUnit findBybuNameIgnoreCase(String bu);
}
