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
public class SODto {
	private String id;
	private String serviceOrder;
	private String teamId;
	private Integer positionCount;
	private String location;
	private String createUser;
	private Date createdDate;
	private String lastModifiedUser;
	private Date updateDate;

	private List<SOCaseDto> cases;
	private List<SOCandidateDto> soCandidates;

}
