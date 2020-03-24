package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository <Team, String>{

    Team findByteamName(String teamName);
}
