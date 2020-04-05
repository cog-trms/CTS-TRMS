package com.cognizant.trms.model.opportunity;

import com.cognizant.trms.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private User panel;
    private String feedback;
    private String interviewStatus;
}
