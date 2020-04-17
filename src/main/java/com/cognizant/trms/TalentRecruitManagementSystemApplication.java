package com.cognizant.trms;

import com.cognizant.trms.model.user.*;
import com.cognizant.trms.repository.user.*;
import com.cognizant.trms.security.AuditorAwareImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Aravindan Dandapani
 */
@SpringBootApplication
public class TalentRecruitManagementSystemApplication {

	private static final Logger log = LogManager.getLogger(TalentRecruitManagementSystemApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(TalentRecruitManagementSystemApplication.class, args);
		log.info("Appllication started");
	}
	@Bean
	public AuditorAware<String> myAuditorProvider() {
		return new AuditorAwareImpl();
	}

	@Bean
	CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository,
			BusinessUnitRepository businessUnitRepository, AccountRepository accountRepository,
			ProgramRepository programRepository, UserRoleRepository userRoleRepository, TeamRepository teamRepository) {
		return args -> {
			Role initRole = roleRepository.findByRole(UserRoles.UN_ASSIGNED.name());
			if (initRole == null) {
				initRole = new Role();
				initRole.setRole(UserRoles.UN_ASSIGNED.name());
				roleRepository.save(initRole);
			}

			// Create Admin and Hiring Manager Roles
			Role adminRole = roleRepository.findByRole(UserRoles.ADMIN.name());
			if (adminRole == null) {
				adminRole = new Role();
				adminRole.setRole(UserRoles.ADMIN.name());
				roleRepository.save(adminRole);

			}

			Role hmRole = roleRepository.findByRole(UserRoles.HIRING_MANAGER.name());
			if (hmRole == null) {
				hmRole = new Role();
				hmRole.setRole(UserRoles.HIRING_MANAGER.name());
				roleRepository.save(hmRole);
			}

			Role pmRole = roleRepository.findByRole(UserRoles.PROGRAM_MANAGER.name());
			if (pmRole == null) {
				pmRole = new Role();
				pmRole.setRole(UserRoles.PROGRAM_MANAGER.name());
				roleRepository.save(pmRole);
			}

			Role tmRole = roleRepository.findByRole(UserRoles.TEAM_MEMBER.name());
			if (tmRole == null) {
				tmRole = new Role();
				tmRole.setRole(UserRoles.TEAM_MEMBER.name());
				roleRepository.save(tmRole);
			}

			BusinessUnit businessUnit = businessUnitRepository.findBybuNameIgnoreCase("CMT");
			if (businessUnit == null) {
				businessUnit = new BusinessUnit();
				businessUnit.setBuName("CMT");
				businessUnitRepository.save(businessUnit);
			}

			// Create an Admin user
			User admin = userRepository.findByEmail("admin@gmail.com");
			if (admin == null) {
				admin = new User().setEmail("admin@gmail.com")
						.setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
						.setFirstName("John").setLastName("Doe").setMobileNumber("9425094250");
						//.setRoles(new HashSet<>(Arrays.asList(adminRole)));
				userRepository.save(admin);
			}
			List<UserRole> adminUserRoles = userRoleRepository.findByUserIdAndRoleId(admin.getId(),
					adminRole.getId());
			if (adminUserRoles.isEmpty()) {
				UserRole adminUserRole = new UserRole()
						// .setUserId(hiringManager.getId())
						// .setRoleId(hmRole.getId())
						.setUser(admin)
						.setRole(adminRole);
				userRoleRepository.save(adminUserRole);
			}

//            UserRole adminUserRole = new UserRole()
//                    .setRoleId(adminRole.getId())
//                    .setUserid(admin.getId());
//            userRoleRepository.save(adminUserRole);
//
//            Optional<User> adminUser = userRepository.findById(admin.getId());
//            if (adminUser.isPresent()){
//
//                Set<Role> roles = new HashSet<>();
//                adminUser.get().setRoles(roles);
//                adminUser.get().getRoles().add(adminRole);
//                Set<UserRole> userroles = new HashSet<>();
//                adminUser.get().setUserroles(userroles);
//                adminUser.get().getUserroles().add(adminUserRole);
//                userRepository.save(adminUser.get());
//            }

			// Create an HiringManager/AccountManager
			User hiringManager = userRepository.findByEmail("hm@gmail.com");
			if (hiringManager == null) {
				hiringManager = new User().setEmail("hm@gmail.com")
						.setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
						.setFirstName("Sanjay").setLastName("Hiring Manager").setMobileNumber("0005543");
						//.setRoles(new HashSet<>(Arrays.asList(initRole)));
				userRepository.save(hiringManager);
			}
			// Create an ProgramManager
			User programManager = userRepository.findByEmail("pm@gmail.com");
			if (programManager == null) {
				programManager = new User().setEmail("pm@gmail.com")
						.setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
						.setFirstName("Aravind").setLastName("Program Manager").setMobileNumber("1234567890");
						//.setRoles(new HashSet<>(Arrays.asList(initRole)));
				userRepository.save(programManager);


			}

			// Create an ProgramManager
			User teamMember1 = userRepository.findByEmail("team1@gmail.com");
			if (teamMember1 == null) {
				teamMember1 = new User().setEmail("team1@gmail.com")
						.setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
						.setFirstName("Vara").setLastName("Team Member1").setMobileNumber("1234567890");
						//.setRoles(new HashSet<>(Arrays.asList(initRole)));
				userRepository.save(teamMember1);
			}

			// Create an ProgramManager
			User teamMember2 = userRepository.findByEmail("team2@gmail.com");
			if (teamMember2 == null) {
				teamMember2 = new User().setEmail("team2@gmail.com")
						.setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
						.setFirstName("Ganesh").setLastName("Team Member2").setMobileNumber("1234567890");
						//.setRoles(new HashSet<>(Arrays.asList(initRole)));
				userRepository.save(teamMember2);
			}

			// Create an Pearson account if it isn't already exist and assign a Hiring
			// Manager
			Account account = accountRepository.findByaccountNameIgnoreCase("Pearson");
			if (account == null) {
				account = new Account();
				account.setAccountName("Pearson");
				account.setBusinessUnit(businessUnit);
				account.setHiringManger(hiringManager);
				// account.setUserId(hiringManager.getId());
				accountRepository.save(account);

				List<UserRole> userRoles = userRoleRepository.findByUserIdAndRoleId(hiringManager.getId(),
						hmRole.getId());
				if (userRoles.isEmpty()) {
					UserRole userRole = new UserRole()
							// .setUserId(hiringManager.getId())
							// .setRoleId(hmRole.getId())
							.setUser(hiringManager).setRole(hmRole).setAccount(account);

					userRoleRepository.save(userRole);
				}
				Optional<User> hm = userRepository.findById(hiringManager.getId());
				if (hm.isPresent()) {

//					Set<Role> roles = new HashSet<>();
//					hm.get().setRoles(roles);
//					hm.get().getRoles().add(hmRole);

//                    Set<UserRole> userroles = new HashSet<>();
//                    hm.get().setUserroles(userroles);
//                    hm.get().getUserroles().add(userRoles);
					userRepository.save(hm.get());
				}
			}

			Program gpProgram = programRepository.findByprogramName("Global Products");
			if (gpProgram == null) {
				gpProgram = new Program().setProgramName("Global Products").setAccount(account)
						.setProgramMgr(programManager);
				programRepository.save(gpProgram);

				List<UserRole> pmUserRoles = userRoleRepository.findByUserIdAndRoleId(programManager.getId(),
						hmRole.getId());
				if (pmUserRoles.isEmpty()) {
					UserRole pmUserRole = new UserRole()
							// .setUserId(programManager.getId())
							// .setRoleId(pmRole.getId())
							.setUser(programManager).setRole(pmRole).setAccount(account).setProgram(gpProgram);

					userRoleRepository.save(pmUserRole);
				}

				Optional<User> pm = userRepository.findById(programManager.getId());
				if (pm.isPresent()) {

//					Set<Role> roles = new HashSet<>();
//					pm.get().setRoles(roles);
//					pm.get().getRoles().add(pmRole);
//                    Set<UserRole> userroles = new HashSet<>();
//                    pm.get().setUserroles(userroles);
//                    pm.get().getUserroles().add(pmUserRoles);
					userRepository.save(pm.get());
				}
			}

			Team plaTeam = teamRepository.findByteamName("PLA");
			if (plaTeam == null) {
				plaTeam = new Team().setTeamName("PLA").setProgram(gpProgram)
						.setTeamMembers(new HashSet<>(Arrays.asList(teamMember1)));
				teamRepository.save(plaTeam);

				List<UserRole> tmRoles = userRoleRepository.findByUserIdAndRoleId(teamMember1.getId(), tmRole.getId());
				if (tmRoles.isEmpty()) {
					UserRole tmUserRole = new UserRole()
							// .setUserId(teamMember1.getId())
							// .setRoleId(tmRole.getId())
							.setUser(teamMember1).setRole(tmRole).setAccount(account).setProgram(gpProgram)
							.setTeam(plaTeam);

					userRoleRepository.save(tmUserRole);
				}
				Optional<User> tm1 = userRepository.findById(teamMember1.getId());
				if (tm1.isPresent()) {

//					Set<Role> roles = new HashSet<>();
//					tm1.get().setRoles(roles);
//					tm1.get().getRoles().add(tmRole);
//                    Set<UserRole> userroles = new HashSet<>();
//                    tm1.get().setUserroles(userroles);
//                    tm1.get().getUserroles().add(tmRoles);
					userRepository.save(tm1.get());
				}
			}
			// Updating Team model with new teamMember2
			Optional<Team> plaTeam1 = teamRepository.findById(plaTeam.getId());
			if (plaTeam1.isPresent()) {
				Set<User> plaTeamMembers = new HashSet<>();
				plaTeamMembers = plaTeam1.get().getTeamMembers();
				plaTeamMembers.add(teamMember2);
				plaTeam1.get().setTeamMembers(plaTeamMembers);
				teamRepository.save(plaTeam1.get());

				List<UserRole> tmRoles2 = userRoleRepository.findByUserIdAndRoleId(teamMember2.getId(), tmRole.getId());
				if (tmRoles2.isEmpty()) {
					UserRole tmUserRole2 = new UserRole()
							// .setUserId(teamMember2.getId())
							// .setRoleId(tmRole.getId())
							.setUser(teamMember2).setRole(tmRole).setAccount(account).setProgram(gpProgram)
							.setTeam(plaTeam);

					userRoleRepository.save(tmUserRole2);
				}
				Optional<User> tm2 = userRepository.findById(teamMember2.getId());
				if (tm2.isPresent()) {

//					Set<Role> roles = new HashSet<>();
//					tm2.get().setRoles(roles);
//					tm2.get().getRoles().add(tmRole);
//                    Set<UserRole> userroles = new HashSet<>();
//                    tm2.get().setUserroles(userroles);
//                    tm2.get().getUserroles().add(tmRoles2);
					userRepository.save(tm2.get());
				}
			}


			// Updating Team model with new teamMember3
			Optional<Team> plaTeam2 = teamRepository.findById(plaTeam.getId());
			if (plaTeam1.isPresent()) {
				Set<User> plaTeamMembers = new HashSet<>();
				plaTeamMembers = plaTeam2.get().getTeamMembers();
				plaTeamMembers.add(programManager);
				plaTeam1.get().setTeamMembers(plaTeamMembers);
				teamRepository.save(plaTeam2.get());

				List<UserRole> tmRoles3 = userRoleRepository.findByUserIdAndRoleId(programManager.getId(), tmRole.getId());
				if (tmRoles3.isEmpty()) {
					UserRole tmUserRole3 = new UserRole()
							.setUser(programManager)
							.setRole(tmRole)
							.setAccount(account)
							.setProgram(gpProgram)
							.setTeam(plaTeam);

					userRoleRepository.save(tmUserRole3);
				}
//				Optional<User> tm2 = userRepository.findById(teamMember2.getId());
//				if (tm2.isPresent()) {
//
//					Set<Role> roles = new HashSet<>();
//					tm2.get().setRoles(roles);
//					tm2.get().getRoles().add(tmRole);
////                    Set<UserRole> userroles = new HashSet<>();
////                    tm2.get().setUserroles(userroles);
////                    tm2.get().getUserroles().add(tmRoles2);
//					userRepository.save(tm2.get());
//				}
			}


		};
	}
}
