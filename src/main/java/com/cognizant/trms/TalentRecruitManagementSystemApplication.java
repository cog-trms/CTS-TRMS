package com.cognizant.trms;

import com.cognizant.trms.model.user.*;
import com.cognizant.trms.repository.user.*;
import com.cognizant.trms.util.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

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
                           UserRoleRepository userRoleRepository) {
        return args -> {
            //Create Admin and Hiring Manager Roles
            Role adminRole = roleRepository.findByRole("ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setRole("ADMIN");
                roleRepository.save(adminRole);
            }

            Role userRole = roleRepository.findByRole("HIRING_MANAGER");
            if (userRole == null) {
                userRole = new Role();
                userRole.setRole("HIRING_MANAGER");
                roleRepository.save(userRole);
            }


            Role initial = roleRepository.findByRole("UN_ASSIGNED");
            if (initial == null) {
                initial = new Role();
                initial.setRole("UN_ASSIGNED");
                roleRepository.save(initial);
            }

            BusinessUnit businessUnit = businessUnitRepository.findBybuName("CMT");
            if (businessUnit == null){
                businessUnit = new BusinessUnit();
                businessUnit.setBuName("CMT");
                businessUnitRepository.save(businessUnit);
            }

            //Create an Admin user
            User admin = userRepository.findByEmail("admin.agencya@gmail.com");
            if (admin == null) {
                admin = new User()
                        .setEmail("admin.agencya@gmail.com")
                        .setPassword("$2a$10$7PtcjEnWb/ZkgyXyxY1/Iei2dGgGQUbqIIll/dt.qJ8l8nQBWMbYO") // "123456"
                        .setFirstName("John")
                        .setLastName("Doe")
                        .setMobileNumber("9425094250")
                        .setRoles(new HashSet<>(Arrays.asList(adminRole)));
                userRepository.save(admin);
            }


            //Create an HiringManager/AccountManager
            User hiringManager = userRepository.findByEmail("5@gmail.com");
            if (hiringManager == null) {
                hiringManager = new User()
                        .setEmail("5@gmail.com")
                        .setPassword("$2a$10$zDSewAAH8orw3knrxmQLCeGXvuERTt5m9M1cpmPIDCzGw21GEkbpO") // "123"
                        .setFirstName("Sanjay")
                        .setLastName("Sanjay")
                        .setMobileNumber("9425094250")
                        .setRoles(new HashSet<>(Arrays.asList(initial)));
                userRepository.save(hiringManager);
            }

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
                            .setRole_id(userRole.getId())
                            .setAccount(account);
                    userRoleRepository.save(userRoles);
                }
               Optional<User> hm = userRepository.findById(hiringManager.getId());
                if (hm.isPresent()){

                    Set<Role> roles = new HashSet<>();
                    hm.get().setRoles(roles);
                    hm.get().getRoles().add(userRole);
                   // hm.get().setRoles(new HashSet<>(Arrays.asList(userRole)));
                    hm.get().setFirstName("FirstName");
                    userRepository.save(hm.get());
                }

               



            }




        };
    }
}
