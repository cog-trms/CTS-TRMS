package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.BusinessUnitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    Author: Aravindan Dandapani
*/
@RestController
@RequestMapping("/api/v1/bu")
@Api(value="trms-application", description="Operations pertaining to BusinessUnit management in the TRMS application")
public class BusinessUnitController {
    @Autowired
    private BusinessUnitService businessUnitService;

    @GetMapping("/all")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAllBu(){
        return Response.ok()
                .setPayload(businessUnitService.getAllBusinessUnits());
    }

    @GetMapping("/id/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getBuById(String Id){
        return Response
                .ok()
                .setPayload(businessUnitService.getBusinessUnitById(Id));
    }

    @GetMapping("/name/{name}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getBuByName(String buName){
        return Response
                .ok()
                .setPayload(businessUnitService.getBusinessUnitByName(buName));
    }

}
