package com.cognizant.trms.model.opportunity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "candidate")
public class Candidate {
    @Id
    private String id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String candidateEmail;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String mobile;
    private String firstName;
    private String lastName;
    private boolean isActive;

}
