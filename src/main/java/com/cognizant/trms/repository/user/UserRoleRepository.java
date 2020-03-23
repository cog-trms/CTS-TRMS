package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.UserRole;
import com.cognizant.trms.model.user.UserRoles;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
    UserRole findByuserid(String userid);
}
