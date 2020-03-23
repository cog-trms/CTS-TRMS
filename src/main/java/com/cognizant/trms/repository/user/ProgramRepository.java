package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface ProgramRepository extends MongoRepository <Program, String>{
    Program findByprogramName(String pgmName);
}
