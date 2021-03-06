package com.cognizant.trms.dto.model.user;

import com.cognizant.trms.model.user.Program;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRoleDto {

    private String id;
   // private String userId;
   // private String roleId;
//    private String accountId;
//    private String programId;
//    private String teamId;
    private UserDto user;
    private RoleDto role;
    private AccountDto account;
    private ProgramDto program;
    private TeamDto team;
}
