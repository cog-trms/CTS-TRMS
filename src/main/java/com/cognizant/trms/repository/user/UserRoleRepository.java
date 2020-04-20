package com.cognizant.trms.repository.user;

import com.cognizant.trms.model.user.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Set;

public interface UserRoleRepository extends MongoRepository<UserRole, String> {
	UserRole findByuserId(String userId);

	List<UserRole> findByUserIdAndRoleId(String userId, String roleId);

	UserRole findByRoleIdAndAccount(String roleId, Account account);

	UserRole findByUserIdAndRoleIdAndAccount(String userId, String roleId, Account account);

	UserRole findByRoleIdAndAccountAndProgram(String roleId, Account account, Program program);
	
	UserRole findByRoleIdAndAccountAndProgramAndTeam(String roleId, Account account, Program program,Team team);

	UserRole findByUserIdAndRoleIdAndAccountAndProgram(String userId, String roleId, Account account, Program program);
	
	UserRole findByUserIdAndRoleIdAndAccountAndProgramAndTeam(String userId, String roleId, Account account, Program program,Team team);

	List<UserRole> findByAccount(Account account);

	List<UserRole> findByProgram(Program program);

	List<UserRole> findByTeam(Team team);

	List<UserRole> findByUser(User user);
	
	List<UserRole> findAccountByUser(User user);
	
	List<UserRole> findProgramByUser(User user);
	

}
