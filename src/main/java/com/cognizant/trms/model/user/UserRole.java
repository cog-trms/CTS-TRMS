package com.cognizant.trms.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "userrole")
public class UserRole {
    @Id
    private String id;
    private String userid;
    private String role_id;
    @DBRef(lazy = true)
    private Account account;
    @DBRef(lazy = true)
    private Program program;
    @DBRef(lazy = true)
    private Team team;
}
