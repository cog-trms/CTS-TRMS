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
public class AccountCreationRequest {

    private String id;
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String accountName;
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String userId;
    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String businessUnitId;

}

