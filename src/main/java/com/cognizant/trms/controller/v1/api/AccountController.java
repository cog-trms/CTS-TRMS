package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.dto.mapper.AccountMapper;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
@RequestMapping("/api/v1/account")
@Api(value="trms-application", description="Operations pertaining to Account management in the TRMS application")
public class AccountController {

    @Autowired
   private AccountService accountService;


    @GetMapping("/account")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAllAccount() throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(accountService.getAllAccounts());
    }

}

