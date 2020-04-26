package com.cognizant.trms.dto.model.opportunity;

import java.util.Date;
import java.util.List;

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
public class SOCaseDto implements Comparable {
	private String id;
	private String soId;
	private String skill;
	private String level;
	private Integer numberOfPosition;

	private String status;
	private Integer numberOfSelected;
	private Integer numberOfFilled;
	
	private String createdBy;
	private Date createdDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	private List<CaseCandidateDto> caseCandidates;

	@Override
	public int compareTo(Object arg0) {
		return this.getId().compareTo(((SOCaseDto) arg0).getId());
	}

}
