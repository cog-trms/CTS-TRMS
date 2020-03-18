package com.cognizant.trms;

import com.cognizant.trms.model.user.Role;
import com.cognizant.trms.model.user.User;
import com.cognizant.trms.repository.user.RoleRepository;
import com.cognizant.trms.repository.user.UserRepository;
import com.cognizant.trms.util.DateUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashSet;
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
    CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            //Create Admin and Passenger Roles
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

        };
    }
}
