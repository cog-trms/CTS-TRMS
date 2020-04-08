package com.cognizant.trms.dto.model.opportunity;

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
public class CandidateDto {
    private String id;
    private String candidateEmail;
    private String mobile;
    private String firstName;
    private String lastName;
    private boolean isActive;
}
