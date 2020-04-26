package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Program;
import com.cognizant.trms.model.user.Team;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {

	Team findByteamName(String teamName);

	Team findByteamNameAndProgramId(String teamName, Program program);
	
	Team findByIdAndProgramId(String id, Program program);
	
	List<Team> findByProgram(Program program);
}
