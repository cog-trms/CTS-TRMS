package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.user.*;
import com.cognizant.trms.model.user.*;
import io.swagger.models.Model;
import org.modelmapper.ModelMapper;

import javax.jws.WebParam;
import java.util.Objects;
import java.util.Optional;


/*
    Author: Aravindan Dandapani
*/
public class UserRoleMapper {

    public static UserRoleDto toFullUserRoleDto(UserRole userRole){
        return new UserRoleDto()
                .setId(userRole.getId())
                //.setUserId(userRole.getUserId())
                .setUser(new ModelMapper().map(userRole.getUser(),UserDto.class))
              .setRole(new ModelMapper().map(Optional.ofNullable(userRole.getRole()).orElse(new Role()), RoleDto.class))
                .setAccount(new ModelMapper().map(Optional.ofNullable(userRole.getAccount()).orElse(new Account()), AccountDto.class))
                .setProgram(new ModelMapper().map(Optional.ofNullable(userRole.getProgram()).orElse(new Program()), ProgramDto.class))
                .setTeam(new ModelMapper().map(Optional.ofNullable(userRole.getTeam()).orElse(new Team()), TeamDto.class));


    }

    public static UserRoleDtoMinimal toUserRoleDto(UserRole userRole) {
        UserRoleDtoMinimal userRoleDtoMinimal = new UserRoleDtoMinimal();
        userRoleDtoMinimal.setUserId(userRole.getUser().getId());
        userRoleDtoMinimal.setFirstName(userRole.getUser().getFirstName());
        userRoleDtoMinimal.setLastName(userRole.getUser().getLastName());
        if (userRole.getRole() != null) {
            userRoleDtoMinimal.setRoleId(userRole.getRole().getId());
            userRoleDtoMinimal.setRoleName(userRole.getRole().getRole());
        }
        if (userRole.getAccount() != null) {
            userRoleDtoMinimal.setAccountId(userRole.getAccount().getId());
            userRoleDtoMinimal.setAccount(userRole.getAccount().getAccountName());
        }
        if (userRole.getProgram() != null) {
            userRoleDtoMinimal.setProgramId(userRole.getProgram().getId());
            userRoleDtoMinimal.setProgram(userRole.getProgram().getProgramName());
        }
        if (userRole.getTeam() != null) {
            userRoleDtoMinimal.setTeamId(userRole.getTeam().getId());
            userRoleDtoMinimal.setTeam(userRole.getTeam().getTeamName());
        }
        return userRoleDtoMinimal;
    }
}
