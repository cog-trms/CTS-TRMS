package com.cognizant.trms.controller.v1.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/*
    Author: Aravindan Dandapani
*/
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SOCreateRequest {

    @NotEmpty(message = "{constraints.NotEmpty.message}")
    private String serviceOrder;
    private String teamId;
    private Integer positionCount;
    private String location;
    private String createdBy;
    List<CaseCreateRequest> cases;

}
