package com.cognizant.trms;

import com.cognizant.trms.model.user.*;
import com.cognizant.trms.repository.user.*;
import com.cognizant.trms.util.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
/**
 * Aravindan Dandapani
 */
@SpringBootApplication
public class TalentRecruitManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(TalentRecruitManagementSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository,
                           UserRepository userRepository,
                           BusinessUnitRepository businessUnitRepository,
                           AccountRepository accountRepository,
                           ProgramRepository programRepository,
                           UserRoleRepository userRoleRepository,
                           TeamRepository teamRepository) {
        return args -> {
            Role initRole = roleRepository.findByRole(UserRoles.UN_ASSIGNED.name());
            if (initRole == null) {
                initRole = new Role();
                initRole.setRole(UserRoles.UN_ASSIGNED.name());
                roleRepository.save(initRole);
            }

            //Create Admin and Hiring Manager Roles
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




            BusinessUnit businessUnit = businessUnitRepository.findBybuName("CMT");
            if (businessUnit == null){
                businessUnit = new BusinessUnit();
                businessUnit.setBuName("CMT");
                businessUnitRepository.save(businessUnit);
            }

            //Create an Admin user
            User admin = userRepository.findByEmail("admin@gmail.com");
            if (admin == null) {
                admin = new User()
                        .setEmail("admin@gmail.com")
                        .setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setMobileNumber("9425094250")
                        .setRoles(new HashSet<>(Arrays.asList(adminRole)));
                userRepository.save(admin);
            }


            //Create an HiringManager/AccountManager
            User hiringManager = userRepository.findByEmail("hm@gmail.com");
            if (hiringManager == null) {
                hiringManager = new User()
                        .setEmail("hm@gmail.com")
                        .setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
                        .setFirstName("Sanjay")
                        .setLastName("Hiring Manager")
                        .setMobileNumber("0005543")
                        .setRoles(new HashSet<>(Arrays.asList(initRole)));
                userRepository.save(hiringManager);
            }
            //Create an ProgramManager
            User programManager = userRepository.findByEmail("pm@gmail.com");
            if (programManager == null) {
                programManager = new User()
                        .setEmail("pm@gmail.com")
                        .setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
                        .setFirstName("Aravind")
                        .setLastName("Program Manager")
                        .setMobileNumber("1234567890")
                        .setRoles(new HashSet<>(Arrays.asList(initRole)));
                userRepository.save(programManager);
            }

            //Create an ProgramManager
            User teamMember1 = userRepository.findByEmail("team1@gmail.com");
            if (teamMember1 == null) {
                teamMember1 = new User()
                        .setEmail("team1@gmail.com")
                        .setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
                        .setFirstName("Vara")
                        .setLastName("Team Member1")
                        .setMobileNumber("1234567890")
                        .setRoles(new HashSet<>(Arrays.asList(initRole)));
                userRepository.save(teamMember1);
            }

            //Create an ProgramManager
            User teamMember2 = userRepository.findByEmail("team2@gmail.com");
            if (teamMember2 == null) {
                teamMember2 = new User()
                        .setEmail("team2@gmail.com")
                        .setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
                        .setFirstName("Ganesh")
                        .setLastName("Team Member2")
                        .setMobileNumber("1234567890")
                        .setRoles(new HashSet<>(Arrays.asList(initRole)));
                userRepository.save(teamMember2);
            }

            // Create an Pearson account if it isn't already exist and assign a Hiring Manager
            Account account = accountRepository.findByaccountName("Pearson");
            if (account == null){
                account = new Account();
                account.setAccountName("Pearson");
                account.setBusinessUnit(businessUnit);
                account.setAccountMgr(hiringManager);
                accountRepository.save(account);

                UserRole userRoles = userRoleRepository.findByuserid(hiringManager.getId());
                if(userRoles == null){
                    userRoles = new UserRole()
                            .setUserid(hiringManager.getId())
                            .setRole_id(hmRole.getId())
                            .setAccount(account);
                    userRoleRepository.save(userRoles);
                }
                Optional<User> hm = userRepository.findById(hiringManager.getId());
                if (hm.isPresent()){

                    Set<Role> roles = new HashSet<>();
                    hm.get().setRoles(roles);
                    hm.get().getRoles().add(hmRole);
                    Set<UserRole> userroles = new HashSet<>();
                    hm.get().setUserroles(userroles);
                    hm.get().getUserroles().add(userRoles);
                    userRepository.save(hm.get());
                }
     }

            Program gpProgram = programRepository.findByprogramName("Global Products");
            if (gpProgram == null){
                gpProgram = new Program()
                    .setProgramName("Global Products")
                    .setAccount(account)
                    .setProgramMgr(programManager);
                programRepository.save(gpProgram);

                UserRole pmUserRoles = userRoleRepository.findByuserid(programManager.getId());
                if(pmUserRoles == null){
                    pmUserRoles = new UserRole()
                            .setUserid(programManager.getId())
                            .setRole_id(pmRole.getId())
                            .setAccount(account)
                            .setProgram(gpProgram);
                    userRoleRepository.save(pmUserRoles);
                }

                Optional<User> pm = userRepository.findById(programManager.getId());
                if (pm.isPresent()){

                    Set<Role> roles = new HashSet<>();
                    pm.get().setRoles(roles);
                    pm.get().getRoles().add(pmRole);
                    Set<UserRole> userroles = new HashSet<>();
                    pm.get().setUserroles(userroles);
                    pm.get().getUserroles().add(pmUserRoles);
                    userRepository.save(pm.get());
                }
            }



            Team plaTeam = teamRepository.findByteamName("PLA");
            if (plaTeam == null){
                plaTeam = new Team()
                        .setTeamName("PLA")
                        .setProgram(gpProgram)
                        .setTeamMembers(new HashSet<>(Arrays.asList(teamMember1)));
                teamRepository.save(plaTeam);

                UserRole tmRoles = userRoleRepository.findByuserid(teamMember1.getId());
                if(tmRoles == null){
                    tmRoles = new UserRole()
                            .setUserid(teamMember1.getId())
                            .setRole_id(tmRole.getId())
                            .setAccount(account)
                            .setProgram(gpProgram)
                            .setTeam(plaTeam);
                    userRoleRepository.save(tmRoles);
                }
                Optional<User> tm1 = userRepository.findById(teamMember1.getId());
                if (tm1.isPresent()){

                    Set<Role> roles = new HashSet<>();
                    tm1.get().setRoles(roles);
                    tm1.get().getRoles().add(tmRole);
                    Set<UserRole> userroles = new HashSet<>();
                    tm1.get().setUserroles(userroles);
                    tm1.get().getUserroles().add(tmRoles);
                    userRepository.save(tm1.get());
                }
            }
            // Updating Team model with new teamMember2
            Optional<Team> plaTeam1 = teamRepository.findById(plaTeam.getId());
            if(plaTeam1.isPresent()){
                Set<User> plaTeamMembers = new HashSet<>();
                plaTeamMembers = plaTeam1.get().getTeamMembers();
                plaTeamMembers.add(teamMember2);
                plaTeam1.get().setTeamMembers(plaTeamMembers);
                teamRepository.save(plaTeam1.get());


                UserRole tmRoles2 = userRoleRepository.findByuserid(teamMember2.getId());
                if(tmRoles2 == null){
                    tmRoles2 = new UserRole()
                            .setUserid(teamMember2.getId())
                            .setRole_id(tmRole.getId())
                            .setAccount(account)
                            .setProgram(gpProgram)
                            .setTeam(plaTeam);
                    userRoleRepository.save(tmRoles2);
                }
                Optional<User> tm2 = userRepository.findById(teamMember2.getId());
                if (tm2.isPresent()){

                    Set<Role> roles = new HashSet<>();
                    tm2.get().setRoles(roles);
                    tm2.get().getRoles().add(tmRole);
                    Set<UserRole> userroles = new HashSet<>();
                    tm2.get().setUserroles(userroles);
                    tm2.get().getUserroles().add(tmRoles2);
                    userRepository.save(tm2.get());
                }
            }

        };
    }
}
