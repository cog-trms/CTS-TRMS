package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.user.RoleDto;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.model.user.UserRoleDto;
import com.cognizant.trms.model.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Aravindan Dandapani
 */
@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
// NULL CHECK NEEDED FOR ALL THE STREAM CODE BELOW
        return new UserDto()
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setMobileNumber(user.getMobileNumber())
                .setRoles(

                            new HashSet<RoleDto>(user
                                    .getRoles()
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .map(role -> new ModelMapper().map(role, RoleDto.class))
                                    .collect(Collectors.toSet()))

                         )
                .setUser_roles(
                        new HashSet<UserRoleDto>(user
                            .getUserroles()
                            .stream()
                                .filter(Objects::nonNull)
                            .map(userRole -> new ModelMapper().map(userRole, UserRoleDto.class))
                            .collect(Collectors.toSet())));
    }

}
