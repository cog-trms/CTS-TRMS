package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface AccountRepository extends MongoRepository <Account, String>{
    Account findByaccountName(String accName);
}
