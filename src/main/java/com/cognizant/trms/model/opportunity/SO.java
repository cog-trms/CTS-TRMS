package com.cognizant.trms.model.opportunity;

import com.cognizant.trms.model.user.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "serviceorder")
public class SO {
    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String serviceOrder;

    private String teamId;
    private Integer positionCount;
    private String location;
    private String createdBy;
//    @DBRef(lazy = true)
//    private List<SOMappedCandidate> candidates;
//    @DBRef(lazy = true)
//    private List<SOCase> cases;
}
