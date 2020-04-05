package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.Program;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
    Author: Aravindan Dandapani
*/
public interface ProgramRepository extends MongoRepository<Program, String> {
	Program findByprogramName(String pgmName);

	Program findByProgramNameAndAccountId(String pgmName, Account account);

	List<Program> findByAccount(Account account);
}
