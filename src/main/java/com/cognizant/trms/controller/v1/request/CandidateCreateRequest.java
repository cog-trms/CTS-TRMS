package com.cognizant.trms.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CandidateCreateRequest {
    private String id;
    @NotEmpty(message = "candidateEmail is empty")
    private String candidateEmail;
    @NotNull(message = "mobile is empty")
    private String mobile;
    @NotNull(message = "firstName is empty")
    private String firstName;
    @NotNull(message = "lastName is empty")
    private String lastName;

    private boolean isActive;
}
