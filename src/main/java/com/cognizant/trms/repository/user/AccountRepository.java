package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.BusinessUnit;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface AccountRepository extends MongoRepository <Account, String>{
    Account findByaccountName(String accName);

    Account findByBusinessUnit(BusinessUnit businessUnit);

    Account findByAccountNameAndBusinessUnit(String accountName, BusinessUnit businessUnit);

}
