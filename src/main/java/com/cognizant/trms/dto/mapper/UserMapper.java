package com.cognizant.trms.dto.mapper;

import com.cognizant.trms.dto.model.user.RoleDto;
import com.cognizant.trms.dto.model.user.UserDto;
import com.cognizant.trms.dto.model.user.UserRoleDto;
import com.cognizant.trms.model.user.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Aravindan Dandapani
 */
@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
//        Optional<Set<Role>> setRoles = Optional.ofNullable(user.getRoles());
//
//       Set<Role> srs =  setRoles.get();
// NULL CHECK NEEDED FOR ALL THE STREAM CODE BELOW
        return new UserDto()
                .setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setMobileNumber(user.getMobileNumber())

                .setRoles(new HashSet<RoleDto>(Optional.ofNullable(user.getRoles()).orElse(Collections.emptySet())
                                    .stream()
                                    .filter(Objects::nonNull)
                                    .map(role -> new ModelMapper().map(role, RoleDto.class))
                                    .collect(Collectors.toSet()))

                    )

                .setUserRoles(
                        new HashSet<UserRoleDto>(Optional.ofNullable(user.getUserroles()).orElse(Collections.emptySet())
                            .stream()
                                .filter(Objects::nonNull)
                            .map(userRole -> new ModelMapper().map(userRole, UserRoleDto.class))
                            .collect(Collectors.toSet())));
    }

}
