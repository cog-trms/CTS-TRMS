package com.cognizant.trms.model.opportunity;

import com.cognizant.trms.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(collection = "interview")
public class Interview {
    @Id
    private String id;
    private String caseCandidateId;
    private String candidateId;
   // private User panel;
    private String panelUserId;
    private String feedback;
    private Date interviewDate;
    private String interviewStatus;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    private Date lastModifiedDate;
}
