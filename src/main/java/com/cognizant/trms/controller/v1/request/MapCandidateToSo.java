package com.cognizant.trms.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MapCandidateToSo {
    private String id;
    @NotEmpty(message = "ServiceOrder is empty")
    private String soId;
    @NotEmpty(message = "CandidateId is empty")
    private String candidateId;
    private boolean isActive;
}
