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
public class CaseCreateRequest {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String skill;
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String level;
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private Integer numberOfPosition;

    public CaseCreateRequest(CaseCreateRequest item) {
    }
}
