package com.cognizant.trms.model.opportunity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "socase")
public class SOCase {
    @Id
    private String id;
    private String soId;
    private String skill;
    private String level;
    private Integer numberOfPosition;

    private String status;
    private Integer numberOfSelected;
    private Integer numberOfFilled;
    private String jobDescription;
    
    @DBRef(lazy = true)
    private List<CaseCandidate> caseCandidates;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModifiedDate;
 
}
