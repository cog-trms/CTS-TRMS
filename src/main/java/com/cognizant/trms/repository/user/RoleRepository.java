package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Arpit Khandelwal.
 */
public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);

}
