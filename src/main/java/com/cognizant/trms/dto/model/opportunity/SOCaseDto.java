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
public class SOCaseDto {
    private String id;
    private String soId;
    private String skill;
    private String level;
    private Integer numberOfPosition;

    private String status;
    private Integer numberOfSelected;
    private Integer numberOfFilled;
}
