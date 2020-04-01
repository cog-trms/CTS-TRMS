package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Account;
import com.cognizant.trms.model.user.UserRole;
import com.cognizant.trms.model.user.UserRoles;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
    UserRole findByuserId(String userId);
    UserRole findByRoleIdAndAccount(String roleId, Account account);
    UserRole findByUserIdAndRoleIdAndAccount(String userId, String roleId, Account account);
}
