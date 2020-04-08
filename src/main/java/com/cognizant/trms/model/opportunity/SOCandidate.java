package com.cognizant.trms.model.opportunity;

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
@Document(collection = "socandidate")
public class SOCandidate {
    @Id
    private String id;
    private String soId;
    //private String candidateId;
    @DBRef(lazy = true)
    private Candidate candidate;
    private boolean isActive = true;
}
