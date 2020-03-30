package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.BURequest;
import com.cognizant.trms.controller.v1.request.UserSignupRequest;
import com.cognizant.trms.dto.model.user.BusinessUnitDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.model.user.BusinessUnit;
import com.cognizant.trms.repository.user.BusinessUnitRepository;
import com.cognizant.trms.service.BusinessUnitService;
import com.cognizant.trms.util.AuthUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Optional;

/*
    Author: Aravindan Dandapani
*/
@RestController
@RequestMapping("/api/v1/bu")
@Api(value="trms-application", description="Operations pertaining to BusinessUnit management in the TRMS application")
public class BusinessUnitController {
    @Autowired
    private BusinessUnitService businessUnitService;
    private BusinessUnitRepository businessUnitRepository;

    @GetMapping("/all")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAllBu(){
        return Response.ok()
                .setPayload(businessUnitService.getAllBusinessUnits());
    }

    @GetMapping("/id/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getBuById(@PathVariable("id") String Id){
        return Response
                .ok()
                .setPayload(businessUnitService.getBusinessUnitById(Id));
    }

    @GetMapping("/name/{name}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getBuByName(@PathVariable("name") String buName){
        return Response
                .ok()
                .setPayload(businessUnitService.getBusinessUnitByName(buName));
    }
    @PostMapping("/")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response createBU(@RequestBody @Valid BURequest buRequest){
        return Response
                .ok()
                .setPayload(businessUnitService.saveBU(buRequest));
    }

    @PutMapping("/")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response updateBU(@RequestBody @Valid BusinessUnitDto businessUnitDto){
        return Response
                .ok()
                .setPayload(businessUnitService.updateBU(businessUnitDto));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response deleteBU(@PathVariable  String id){
        return Response
                .ok()
                .setPayload(businessUnitService.deleteBU(id));
        }

}
