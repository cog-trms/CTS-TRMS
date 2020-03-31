package com.cognizant.trms.controller.v1.api;

import com.cognizant.trms.controller.v1.request.AccountCreationRequest;
import com.cognizant.trms.dto.mapper.AccountMapper;
import com.cognizant.trms.dto.model.user.AccountDto;
import com.cognizant.trms.dto.response.Response;
import com.cognizant.trms.repository.user.AccountRepository;
import com.cognizant.trms.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/*
    Author: Aravindan Dandapani
*/
@RestController
@RequestMapping("/api/v1/accounts")
@Api(value="trms-application", description="Operations pertaining to Account management in the TRMS application")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/account/all")
    @ApiOperation(value = "Lists all accounts accross BU", authorizations = {@Authorization(value = "apiKey")})
    public Response getAllAccount() throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(accountService.getAllAccounts());
    }

    @GetMapping("/account/name/{accountName}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAccountByName(@PathVariable("accountName") String accountName) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(accountService.findByaccountName(accountName));
    }

    @GetMapping("/account/id/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAccountById(@PathVariable("id") String id) throws JsonProcessingException {
        return Response
                .ok()
                .setPayload(accountService.getAccountById(id));
    }

    @GetMapping("/account/buid/{buId}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAccountsByBuId(@PathVariable("buId") String buId){

        return Response
                .ok()
                .setPayload(accountService.getAccountByBusinessUnitId(buId));
    }

    @GetMapping("/account/buname/{name}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAccountsByBuName(@PathVariable("name") String buName){
        return Response
                .ok()
                .setPayload(accountService.getAccountByBusinessUnitId(buName));
    }

    @PostMapping("/account")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response createAccount(@RequestBody @Valid AccountCreationRequest accountCreationRequest){
        return Response
                .ok()
                .setPayload(accountService.createAccount(accountCreationRequest));
    }

    @PutMapping("/account")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response updateAccount(@RequestBody @Valid AccountCreationRequest accountCreationRequest){
        return Response
                .ok()
                .setPayload(accountService.updateAccount(accountCreationRequest));
    }
}

