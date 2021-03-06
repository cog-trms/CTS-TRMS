package com.cognizant.trms.dto.model.user;

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
public class UserRoleDtoMinimal {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String roleId;
    private String roleName;
    private String account;
    private String program;
    private String team;
    private String accountId;
    private String programId;
    private String teamId;

}
