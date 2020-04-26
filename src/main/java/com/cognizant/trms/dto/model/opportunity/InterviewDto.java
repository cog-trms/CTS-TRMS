package com.cognizant.trms.dto.model.opportunity;

import java.util.Date;

import com.cognizant.trms.model.user.User;
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
public class InterviewDto {
    private String id;
    private String caseCandidateId;
    private String candidateId;
    //private User panel;
    private String panelUserId;
    private String feedback;
    private Date interviewDate;
    private String interviewStatus;
    
    private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
}
