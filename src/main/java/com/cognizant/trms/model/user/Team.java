package com.cognizant.trms.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "team")
@CompoundIndexes({
        @CompoundIndex(name = "team_pgm", def = "{'teamName' : 1, 'program.Id': 1}")
})
public class Team {
    @Id
    private String id;
    private String teamName;
    @DBRef(lazy = true)
    private Program program;
    @DBRef(lazy = true)
    private Set<User> teamMembers;
}
