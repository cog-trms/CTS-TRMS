package com.cognizant.trms.model.opportunity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "casecandidate")
public class CaseMappedCandidate {
    @Id
    private String id;
    private String soCaseId;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String soMappedCandidateId;
    private String status;
    private boolean onBoarded;
    List<Interview> interviews;
}
