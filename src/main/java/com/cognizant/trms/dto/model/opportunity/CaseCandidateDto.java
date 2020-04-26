package com.cognizant.trms.dto.model.opportunity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

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
public class CaseCandidateDto {

    private String id;
    private String soCaseId;
    private String soMappedCandidateId;
    private String status;
    private boolean onBoarded;
    List<InterviewDto> interviews;
    
    private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;

}
